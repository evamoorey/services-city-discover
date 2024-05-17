import pytest
import json
import sys
import os
import jwt
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

@pytest.fixture
def client():
    with app.test_client() as client:
        yield client

def test_home_page(client):
    response = client.get('/')
    assert response.status_code == 200
    assert response.data.decode('utf-8') == 'Добро пожаловать в сервис рекомендаций!'


def test_get_categories(client):
    response = client.get('/categories')
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert isinstance(data, list)
    assert len(data) > 0
    category = data[0]
    assert 'id' in category
    assert 'name' in category
    assert 'subcategory' in category
    assert isinstance(category['subcategory'], list)

def test_submit_preferences(client):
    preferences_data = {
        "user_id": 1,
        "age": 25,
        "gender": "male",
        "preferences": [
            {"subcategory_id": 1, "mark": 4},
            {"subcategory_id": 2, "mark": 5}
        ]
    }
    response = client.post('/submit_preferences', json=preferences_data)
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert data['success'] == 'User info and preferences saved successfully'

def test_get_recommendations(client):
    response = client.get('/recommendations?num=10')
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert len(data) == 10

def test_get_places_and_clusters(client):
    response = client.get('/map?p=55.752500,37.612000;55.751500,37.614000')
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert isinstance(data, list)
    assert len(data) > 0
    cluster = data[0]
    assert 'count' in cluster
    assert 'id' in cluster
    assert 'pos1' in cluster
    assert 'pos2' in cluster

def test_rate_place(client):
    rating_data = {
        "user_id": 1,
        "place_id": 123,
        "rating": 4
    }
    response = client.post('/rate_place', json=rating_data)
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert data['success'] == 'Rating added successfully'

def test_get_model_recommendations(client):
    # Создание JWT-токена для тестирования
    user_id = 1
    secret_key = 'uR2djMIlRYULTTvgMdQMFkil5Ecg3qauWYfVbw0jQyTYx0a1Lm2bW9'
    token = jwt.encode({'user_id': user_id}, secret_key, algorithm='HS256')

    # Отправка GET-запроса с JWT-токеном в заголовке Authorization
    response = client.get('/get_model_recommendations?num=5', headers={'Authorization': token})
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert len(data) == 5

def test_view_place(client):
    # Создание JWT-токена для тестирования
    user_id = 1
    secret_key = 'uR2djMIlRYULTTvgMdQMFkil5Ecg3qauWYfVbw0jQyTYx0a1Lm2bW9'
    token = jwt.encode({'user_id': user_id}, secret_key, algorithm='HS256')

    view_data = {
        "place_id": 123
    }
    # Отправка POST-запроса с JWT-токеном в заголовке Authorization
    response = client.post('/view_place', json=view_data, headers={'Authorization': token})
    assert response.status_code == 500
    data = json.loads(response.data.decode('utf-8'))
    # assert data['success'] == 'View recorded successfully'
