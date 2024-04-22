import pandas as pd
from geopy.distance import geodesic

def prepare_inference_data(user_id, user_features, all_places):
    """
    Creates a DataFrame for inference for a single user considering all unrated places.

    Args:
        user_id (int): ID of the user for whom recommendations are being generated.
        user_features (dict): Demographic data of the user (e.g., age and gender).
        all_places (DataFrame): DataFrame of all possible places with their information (including names and addresses).

    Returns:
        DataFrame: A DataFrame suitable for the prediction model.
    """
    # Create a list of dictionaries, where each dictionary corresponds to one row in the future DataFrame
    data_rows = [pd.DataFrame({
        'user_id': user_id,
        'age': user_features['age'],
        'gender': user_features['gender'],
        'category': row['category'],
        'subcategory': row['subcategory'],
        'place_id': row['place_id']
    }) for index, row in all_places.iterrows()]

    # Convert the list of dictionaries to a DataFrame
    data = pd.DataFrame(pd.concat(data_rows))
    data['gender'] = data['gender'].astype('category')
    data['category'] = data['category'].astype('category')
    data['subcategory'] = data['subcategory'].astype('category')

    return data

def filter_places_by_location(places, user_location, limit):
    """
    Filters places based on their distance from the user's location.

    Args:
        places (DataFrame): DataFrame of places with their information.
        user_location (str): User's location in the format "latitude,longitude".
        limit (int): Maximum number of places to return.

    Returns:
        DataFrame: A DataFrame of places sorted by distance from the user's location, limited to the specified number.
    """
    user_lat, user_lon = map(float, user_location.split(','))
    user_coords = (user_lat, user_lon)

    # Calculate the distance from the user to each place and filter
    places['distance'] = places.apply(lambda row: geodesic(user_coords, (row['pos1'], row['pos2'])).km, axis=1)
    return places.sort_values('distance').head(limit)