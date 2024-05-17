from flask import Flask, jsonify, request
import database
import modeling
import jwt
import base64

app = Flask(__name__)


@app.route('/get_model_recommendations', methods=['GET'])
def model_recommendations():
    """
    Endpoint for getting model recommendations.

    Query parameters:
        - offset (int): Offset for pagination. Default is 0.
        - limit (int): Maximum number of recommendations to return. Default is 1000.
        - num (int): Number of recommendations to return. Default is 20.
        - p (str): User's location in the format "latitude,longitude". Default is None.

    Headers:
        - Authorization (str): JWT token containing user information.

    Returns:
        JSON response containing the recommendations or an error message with HTTP status code.
    """
    offset = request.args.get('offset', default=0, type=int)
    limit = request.args.get('limit', default=1000, type=int)
    num = request.args.get('num', default=20, type=int)
    user_location = request.args.get('p', default=None, type=str)

    token = request.headers.get('Authorization')

    if token is None:
        return jsonify({'error': 'Missing JWT token'}), 400

    try:
        secret_key = 'uR2djMIlRYULTTvgMdQMFkil5Ecg3qauWYfVbw0jQyTYx0a1Lm2bW9'
        decoded_token = jwt.decode(token, secret_key, algorithms=['HS256'])
        user_id = decoded_token['user_id']
        print(user_id)
    except jwt.ExpiredSignatureError:
        return jsonify({'error': 'JWT token has expired'}), 401
    except jwt.InvalidTokenError:
        return jsonify({'error': 'Invalid JWT token'}), 401

    recommendations = modeling.get_recommendations(user_id, offset, limit, num, user_location)

    if isinstance(recommendations, tuple):
        return recommendations
    else:
        return jsonify(recommendations)



@app.route('/categories', methods=['GET'])
def categories():
    """
    Endpoint for getting categories and subcategories.

    Returns:
        JSON response containing the categories and subcategories.
    """
    categories_json = database.get_categories_with_subcategories()
    return jsonify(categories_json)

@app.route('/submit_preferences', methods=['POST'])
def submit_preferences():
    """
    Endpoint for submitting user preferences.

    Expected request format:
    {
      "user_id": 1,
      "age": 17,
      "gender": 'male',
      "preferences": [
        {"subcategory_id": 41, "mark": 5},
        {"subcategory_id": 42, "mark": 4},
        {"subcategory_id": 43, "mark": 3}
      ]
    }

    Returns:
        JSON response indicating success or an error message with HTTP status code.
    """
    data = request.json
    if not data or 'user_id' not in data or 'preferences' not in data or 'age' not in data or 'gender' not in data:
        return jsonify({"error": "Missing data"}), 400

    user_id = data['user_id']
    age = data['age']
    gender = data['gender']
    if gender == 'male':
        gender = 'Мужской'
    else:
        gender = 'Женский'
    preferences = data['preferences']

    database.update_user_info('places.db', user_id, age, gender)

    if database.save_user_preferences('places.db', user_id, preferences):
        return jsonify({"success": "User info and preferences saved successfully"})
    else:
        return jsonify({"error": "Failed to save user info and preferences"}), 500

@app.route('/recommendations', methods=['GET'])
def recommendations():
    """
    Endpoint for getting place recommendations.

    Query parameters:
        - num (int): Number of recommendations to return. Default is 20.

    Returns:
        JSON response containing the recommended places or an error message with HTTP status code.
    """
    num = request.args.get('num', default=20, type=int)

    places = database.get_places_with_detailed_info(num=num)
    if places:
        return jsonify(places)
    else:
        return jsonify({"error": "No places found"}), 404

@app.route('/map', methods=['GET'])
def places_and_clusters():
    """
    Endpoint for getting places and clusters within a specified area.

    Query parameters:
        - p (str): Coordinates of the area in the format "top_left_lat,top_left_lon;bottom_right_lat,bottom_right_lon".

    Returns:
        JSON response containing the places and clusters or an error message with HTTP status code.
    """
    coords = request.args.get('p')

    if not coords:
        return jsonify({"error": "Missing coordinates"}), 400

    try:
        top_left, bottom_right = coords.split(';')
        pos2_max, pos1_min = map(float, top_left.split(','))
        pos2_min, pos1_max = map(float, bottom_right.split(','))
    except (ValueError, TypeError):
        return jsonify({"error": "Invalid coordinates format"}), 400

    area = database.haversine(pos1_min, pos2_min, pos1_max, pos2_min) * database.haversine(pos1_min, pos2_min, pos1_min, pos2_max)

    if area <= 1:
        cluster_radius = 0.05
    elif area <= 7:
        cluster_radius = 0.1
    elif area <= 15:
        cluster_radius = 0.15
    elif area <= 25:
        cluster_radius = 0.25
    else:
        cluster_radius = 0.45

    result = database.get_places_and_clusters(pos2_max, pos1_min, pos2_min, pos1_max, cluster_radius)
    return jsonify(result)

@app.route('/rate_place', methods=['POST'])
def rate_place():
    """
    Endpoint for rating a place by a user.

    Expected request format:
    {
      "user_id": 1,
      "place_id": 123,
      "rating": 4
    }

    Returns:
        JSON response indicating success or an error message with HTTP status code.
    """
    data = request.json
    if not data or 'user_id' not in data or 'place_id' not in data or 'rating' not in data:
        return jsonify({"error": "Missing data"}), 400

    user_id = data['user_id']
    place_id = data['place_id']
    rating = data['rating']

    if database.add_user_rating(user_id, place_id, rating):
        return jsonify({"success": "Rating added successfully"})
    else:
        return jsonify({"error": "Failed to add rating"}), 500

@app.route('/add_user_place', methods=['POST'])
def add_user_place():
    """
    Endpoint for adding a user-defined place.
    """
    place_data = request.get_json()
    if not place_data:
        return jsonify({"error": "No data provided"}), 400

    required_fields = ["category_id", "subcategory_id", "name", "address", "rating", "image", "description", "reviews_count", "pos1", "pos2"]
    if not all(field in place_data for field in required_fields):
        return jsonify({"error": "Missing one or more required fields"}), 400

    # Вызов функции добавления места из database.py
    success = database.add_place(place_data)
    if success:
        return jsonify({"success": "Place added successfully"}), 201
    else:
        return jsonify({"error": "Failed to add place"}), 500

@app.route('/view_place', methods=['POST'])
def view_place():
    """
    Endpoint for recording a user's view of a place's detailed information.

    Expected request format:
    {
      "user_id": 1,
      "place_id": 123
    }

    Returns:
        JSON response indicating success or an error message with HTTP status code.
    """
    data = request.json
    if not data or 'user_id' not in data or 'place_id' not in data:
        return jsonify({"error": "Missing data"}), 400

    user_id = data['user_id']
    place_id = data['place_id']

    if database.add_user_view(user_id, place_id):
        return jsonify({"success": "View recorded successfully"})
    else:
        return jsonify({"error": "Failed to record view"}), 500

@app.route('/')
def home():
    """
    Home page endpoint.

    Returns:
        Welcome message.
    """
    return "Добро пожаловать в сервис рекомендаций!"

if __name__ == '__main__':
    place_file_path = '../CITY_DISCOVER_APP/places_data.parquet'
    visit_file_path = '../CITY_DISCOVER_APP/visit_data.parquet'
    user_file_path = '../CITY_DISCOVER_APP/user_data.parquet'
    database.create_tables()
    app.run()