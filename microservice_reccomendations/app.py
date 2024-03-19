from flask import Flask, jsonify
import database  # Импортируем модуль базы данных

app = Flask(__name__)

@app.route('/recommendations', methods=['GET'])
def recommendations():
    places = database.get_random_places(num=20)
    if places:
        return jsonify(places)
    else:
        return jsonify({"error": "No places found"}), 404

@app.route('/categories', methods=['GET'])
def categories():
    categories_json = database.get_categories_with_subcategories()
    return jsonify(categories_json)


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

