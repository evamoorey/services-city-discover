import pickle
from catboost import CatBoostClassifier
from sklearn.neighbors import NearestNeighbors
from flask import jsonify
import database
import preprocessing
import pandas as pd
from geopy.distance import geodesic

def build_index(embeddings, n_neighbors=10):
    """
    Builds an index for fast nearest neighbor search.

    Args:
        embeddings (numpy.ndarray): Embeddings of items to build the index on.
        n_neighbors (int): Number of nearest neighbors to consider. Default is 10.

    Returns:
        NearestNeighbors: The built index for nearest neighbor search.
    """
    index = NearestNeighbors(n_neighbors=n_neighbors, metric='cosine')
    index.fit(embeddings)
    return index

def get_similar_places(user_embedding, place_embeddings, index, n_recommendations=10):
    """
    Finds similar places for a given user based on embeddings.

    Args:
        user_embedding (numpy.ndarray): Embedding of the user.
        place_embeddings (numpy.ndarray): Embeddings of places.
        index (NearestNeighbors): The index for nearest neighbor search.
        n_recommendations (int): Number of recommendations to return. Default is 10.

    Returns:
        list: List of indices of similar places.
    """
    _, indices = index.kneighbors([user_embedding])
    similar_place_ids = indices[0].tolist()
    return similar_place_ids

def cold_start_recommendations(user_id, num, user_location):
    """
    Generates cold start recommendations based on user preferences.

    Args:
        user_id (int): ID of the user.
        num (int): Number of recommendations to return.
        user_location (str): User's location in the format "latitude,longitude".

    Returns:
        tuple: A tuple containing the recommended places in JSON format and the HTTP status code.
    """
    preferences = database.get_user_preferences(user_id)
    if not preferences:
        return jsonify({"error": "No preferences found for user"}), 404

    top_preferences = [pref for pref in preferences if pref['mark'] >= 4]

    place_ids = set()
    for pref in top_preferences:
        place_ids.update(database.get_places_by_subcategory(pref['subcategory_id']))

    if not place_ids:
        return jsonify({"error": "No places found matching the preferences"}), 404

    places = pd.DataFrame(database.get_places_by_ids(list(place_ids)))

    if user_location:
        places = preprocessing.filter_places_by_location(places, user_location, num)

    recommended_places = places.head(num)
    if recommended_places.empty:
        return jsonify({"error": "No places found after filtering by location"}), 404

    print(recommended_places.columns)

    places = database.get_places_by_ids(list(recommended_places['id']))

    return places

def get_recommendations(user_id, offset, limit, num, user_location):
    """
    Generates personalized recommendations for a user.

    Args:
        user_id (int): ID of the user.
        offset (int): Offset for pagination.
        limit (int): Maximum number of recommendations to return.
        num (int): Number of recommendations to return.
        user_location (str): User's location in the format "latitude,longitude".

    Returns:
        dict or tuple: A dictionary containing the recommended places or a tuple with an error message and HTTP status code.
    """
    if not database.check_user_history(user_id):
        print('coldstart')
        return cold_start_recommendations(user_id, num, user_location)

    all_places = pd.DataFrame(database.get_places())
    all_places = all_places.rename(columns={'id': 'place_id'})

    user_data = database.get_user_info(user_id)
    if user_data is None:
        return jsonify({"error": f"No such user {user_id}"}), 404

    user_data_dict = dict(user_data)
    user_data_df = pd.DataFrame([user_data_dict])

    user_features = {'age': user_data_df['age'], 'gender': user_data_df['gender']}
    user_features = pd.DataFrame(user_features)

    with open('svd_model.pkl', 'rb') as file:
        user_embeddings, place_embeddings = pickle.load(file)

    catboost_model = CatBoostClassifier()
    catboost_model.load_model('catboost_model.pkl')

    place_index = build_index(place_embeddings, n_neighbors=limit)

    user_embedding = user_embeddings[user_data_df.index[0]]
    similar_place_indices = get_similar_places(user_embedding, place_embeddings, place_index, n_recommendations=limit)

    filtered_places = all_places[all_places['place_id'].isin(similar_place_indices)]

    if user_location is not None and user_location != '':
        user_lat, user_lon = map(float, user_location.split(','))
        user_coords = (user_lat, user_lon)
        filtered_places['distance'] = filtered_places.apply(lambda row: geodesic(user_coords, (row['pos2'], row['pos1'])).km, axis=1)
        filtered_places = filtered_places.sort_values('distance').head(limit)

    X_infer = preprocessing.prepare_inference_data(user_id, user_features, filtered_places)

    X_infer['gender'] = X_infer['gender'].map({'Мужской': 0, 'Женский': 1})
    X_infer['category'] = X_infer['category'].cat.codes
    X_infer['subcategory'] = X_infer['subcategory'].cat.codes

    user_embedding = user_embeddings[user_data_df.index[0]]
    place_embeddings_infer = place_embeddings[X_infer['place_id'].tolist()]

    user_embedding_features = pd.DataFrame(user_embedding.reshape(1, -1), columns=[f'user_embedding_{i}' for i in range(user_embedding.shape[0])])
    place_embedding_features = pd.DataFrame(place_embeddings_infer, columns=[f'place_embedding_{i}' for i in range(place_embeddings_infer.shape[1])])

    X_infer.reset_index(drop=True, inplace=True)
    user_embedding_features.reset_index(drop=True, inplace=True)
    place_embedding_features.reset_index(drop=True, inplace=True)

    X_infer_with_embeddings = pd.concat([X_infer[['age', 'gender', 'category', 'subcategory']], user_embedding_features, place_embedding_features], axis=1)

    predictions = catboost_model.predict_proba(X_infer_with_embeddings)[:, 1]

    X_infer['probability'] = predictions
    X_infer_sorted = X_infer.sort_values(by='probability', ascending=False)

    end_index = min(offset + num, limit)
    is_end = end_index >= limit

    if is_end:
        top_n_recommendations = X_infer_sorted.tail(num)
    else:
        top_n_recommendations = X_infer_sorted.iloc[offset:end_index]

    if not top_n_recommendations.empty:
        places = database.get_places_by_ids(list(top_n_recommendations['place_id']))
        return places
    else:
        return {"error": f"No places found for user {user_id}"}