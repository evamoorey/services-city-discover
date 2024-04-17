from flask import Flask, jsonify
from flask import request, jsonify
import database  # Импортируем модуль базы данных
import pickle
import catboost
import pandas as pd

app = Flask(__name__)


def prepare_inference_data(user_id, user_features, all_places):
    """
    Создает DataFrame для инференса для одного пользователя с учетом всех неоцененных мест.
    user_id: ID пользователя, для которого генерируются рекомендации
    user_features: Демографические данные пользователя (например, возраст и пол)
    all_places: DataFrame всех возможных мест с их информацией (включая названия и адреса)
    Возвращает DataFrame, подходящий для модели предсказания.
    """
    # Создаем список словарей, где каждый словарь соответствует одной строке в будущем DataFrame
    data_rows = [pd.DataFrame({
        'user_id': user_id,
        'age': user_features['age'],
        'gender': user_features['gender'],
        'place_id': row['id']  # Используем place_id напрямую из all_places
    }) for index, row in all_places.iterrows()]

    # Преобразуем список словарей в DataFrame
    data = pd.DataFrame(pd.concat(data_rows))
    data['place_id'] = data['place_id'].astype('category')
    data['gender'] = data['gender'].astype('category')

    return data


@app.route('/get_model_rcommendations', methods=['GET'])
def model_recommendations():
    """
    Функция выдачи рекомендаций с помощью предобученной модели Гибридной рекомендательной системы основанной на Catboost.
    На входе запрос принимает id пользователя и кол во рекомендаций для выдачи моделью.
    """

    # Получаем параметр num из query-строки, используем 20 как значение по умолчанию
    num = request.args.get('num', default=20, type=int)
    user_id = request.args.get('user_id', default='user_id_123123123123', type=str)

    all_places = pd.DataFrame(database.get_places())
    # Подготовка данных для инференса
    user_data = database.get_user_info(user_id)
    print(user_data)
    if user_data == None:
        return jsonify({"error": f"No such user {user_id}"}), 404

    user_data_dict = dict(user_data)
    # Создаем DataFrame из словаря
    user_data_df = pd.DataFrame([user_data_dict])

    user_features = {'age': user_data_df['age'], 'gender': user_data_df['gender']}
    user_features = pd.DataFrame(user_features)
    X_infer = prepare_inference_data(user_id, user_features, all_places)

    # Загрузка модели
    with open('model.pkl', 'rb') as model_file:
        inference_model = pickle.load(model_file)

    # Предполагается, что X_infer - это DataFrame, подготовленный для инференса,
    # и predictions - массив вероятностей положительного класса для каждого места
    # Положительным считается значение где пользователь оценит место на оценку >=4
    predictions = inference_model.predict_proba(X_infer)[:, 1]

    # Добавляем вероятности как новый столбец в X_infer
    X_infer['probability'] = predictions

    # Сортируем места по убыванию вероятности
    X_infer_sorted = X_infer.sort_values(by='probability', ascending=False)

    # Выбираем топ-N мест
    top_n_recommendations = X_infer_sorted.head(num)

    if not(top_n_recommendations.empty):
        return database.get_places_by_ids(list(top_n_recommendations['place_id']))
    else:
        return jsonify({"error": f"No places found for user {user_id}"}), 404




@app.route('/categories', methods=['GET'])
def categories():
    categories_json = database.get_categories_with_subcategories()
    return jsonify(categories_json)



@app.route('/submit_preferences', methods=['POST'])
def submit_preferences():
    """Формат ожмдаемого запроса:
        {
          "user_id": '1',
          "age": 17,
          "gender": 'male',
          "preferences": [
            {"subcategory_id": 41, "mark": 5},
            {"subcategory_id": 42, "mark": 4},
            {"subcategory_id": 43, "mark": 3}s
          ]
        }
"""
    data = request.json
    if not data or 'user_id' not in data or 'preferences' not in data or 'age' not in data or 'gender' not in data:
        return jsonify({"error": "Missing data"}), 400

    user_id = data['user_id']
    age = data['age']
    gender = data['gender']
    preferences = data['preferences']

    # Обновляем информацию о пользователе
    database.update_user_info('places.db', user_id, age, gender)

    # Сохраняем предпочтения пользователя
    if database.save_user_preferences('places.db', user_id, preferences):
        return jsonify({"success": "User info and preferences saved successfully"})
    else:
        return jsonify({"error": "Failed to save user info and preferences"}), 500

@app.route('/recommendations', methods=['GET'])
def recommendations():
    # Получаем параметр num из query-строки, используем 20 как значение по умолчанию
    num = request.args.get('num', default=20, type=int)

    places = database.get_places_with_detailed_info(num=num)
    if places:
        return jsonify(places)
    else:
        return jsonify({"error": "No places found"}), 404

@app.route('/map', methods=['GET'])
def places_and_clusters():
    coords = request.args.get('p')

    if not coords:
        return jsonify({"error": "Missing coordinates"}), 400

    try:
        top_left, bottom_right = coords.split(';')
        pos2_max, pos1_min = map(float, top_left.split(','))
        pos2_min, pos1_max = map(float, bottom_right.split(','))
    except (ValueError, TypeError):
        return jsonify({"error": "Invalid coordinates format"}), 400

    # Вычисляем площадь прямоугольника
    area = database.haversine(pos1_min, pos2_min, pos1_max, pos2_min) * database.haversine(pos1_min, pos2_min, pos1_min, pos2_max)

    # Динамически настраиваем cluster_radius на основе площади
    if area <= 1:
        cluster_radius = 0.05  # Меньший радиус для малой площади
    elif area <= 7:
        cluster_radius = 0.1
    elif area <= 15:
        cluster_radius = 0.15
    elif area <= 25:
        cluster_radius = 0.25  # Средний радиус для средней площади
    else:
        cluster_radius = 0.45  # Больший радиус для большой площади

    result = database.get_places_and_clusters(pos2_max, pos1_min, pos2_min, pos1_max, cluster_radius)
    return jsonify(result)

@app.route('/')
def home():
    return "Добро пожаловать в сервис рекомендаций!"

if __name__ == '__main__':
    place_file_path = '../CITY_DISCOVER_APP/places_data.parquet'
    visit_file_path = '../CITY_DISCOVER_APP/visit_data.parquet'
    user_file_path = '../CITY_DISCOVER_APP/user_data.parquet'
    database.create_tables()
    # database.insert_places_from_parquet(place_file_path)
    # database.insert_users_and_visits_from_parquet(user_file_path,visit_file_path)
    app.run(debug=True)


#%%
