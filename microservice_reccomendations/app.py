from flask import Flask, jsonify
from flask import request, jsonify
import database  # Импортируем модуль базы данных

app = Flask(__name__)


@app.route('/recommendations', methods=['GET'])
def recommendations():
    # Получаем параметр num из query-строки, используем 20 как значение по умолчанию
    num = request.args.get('num', default=20, type=int)

    places = database.get_random_places(num=num)
    if places:
        return jsonify(places)
    else:
        return jsonify({"error": "No places found"}), 404

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
