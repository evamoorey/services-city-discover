import pandas as pd
from surprise import SVD, Dataset, Reader
from catboost import CatBoostClassifier, Pool
import pickle
import datetime
import database

# Установка даты последнего обновления модели
last_update_date = datetime.date(2023, 1, 1)  # Пример: 1 января 2023 года

# Функция для обучения модели SVD
def train_svd(ratings_data):
    reader = Reader(rating_scale=(1, 5))
    data = Dataset.load_from_df(ratings_data[['user_id', 'place_id', 'rating']], reader)

    algo = SVD()
    trainset = data.build_full_trainset()
    algo.fit(trainset)

    user_embeddings = algo.pu
    place_embeddings = algo.qi

    return user_embeddings, place_embeddings

# Функция для обучения модели CatBoost
def train_catboost(X, y):
    model = CatBoostClassifier(iterations=100, depth=4, learning_rate=0.1, loss_function='MultiClass')
    model.fit(X, y, cat_features=['gender', 'category', 'subcategory'])
    return model

# Функция для переобучения модели
def retrain_model():
    global last_update_date

    # Получение текущей даты
    current_date = datetime.date.today()

    # Проверка, прошло ли 60 дней с момента последнего обновления модели
    if (current_date - last_update_date).days >= 60:
        print("Пора переобучить модель!")

        # Загрузка данных из базы данных
        data_visit = pd.DataFrame(database.get_all_visits())
        data_places = pd.DataFrame(database.get_all_places())
        data_users = pd.DataFrame(database.get_all_users())

        data_places.rename(columns={'id': 'place_id'}, inplace=True)
        data = data_visit.merge(data_places, how='left', on='place_id')
        data_users = data_users.rename(columns={'id': 'user_id'})
        data = data.merge(data_users, how='left', on='user_id')

        # Подготовка данных для обучения SVD
        ratings_data = data[['user_id', 'place_id', 'user_rating']]
        ratings_data.columns = ['user_id', 'place_id', 'rating']

        # Обучение модели SVD
        user_embeddings, place_embeddings = train_svd(ratings_data)

        # Подготовка данных для обучения CatBoost
        train_data = data.copy()

        # Фильтруем train_data, оставляя только пользователей и места, присутствующие в ratings_data
        train_data = train_data[train_data['user_id'].isin(ratings_data['user_id']) & train_data['place_id'].isin(ratings_data['place_id'])]

        # Создаем словарь для сопоставления идентификаторов пользователей с их векторными представлениями
        user_embedding_map = {user_id: embedding for user_id, embedding in zip(ratings_data['user_id'], user_embeddings)}

        # Получаем векторные представления пользователей из словаря user_embedding_map
        train_data['user_embedding'] = train_data['user_id'].map(user_embedding_map)

        # Создаем словарь для сопоставления идентификаторов мест с их векторными представлениями
        place_embedding_map = {place_id: embedding for place_id, embedding in zip(ratings_data['place_id'], place_embeddings)}

        # Получаем векторные представления мест из словаря place_embedding_map
        train_data['place_embedding'] = train_data['place_id'].map(place_embedding_map)

        # Удаляем строки с отсутствующими значениями user_embedding и place_embedding
        train_data.dropna(subset=['user_embedding', 'place_embedding'], inplace=True)

        # Преобразование векторных представлений в числовой формат
        train_data['user_embedding'] = train_data['user_embedding'].apply(lambda x: list(map(float, x)))
        train_data['place_embedding'] = train_data['place_embedding'].apply(lambda x: list(map(float, x)))

        # Разделение признаков и целевой переменной
        X = train_data[['age', 'gender', 'user_embedding', 'place_embedding', 'category', 'subcategory']]
        y = train_data['user_rating']

        # Преобразование категориальных признаков
        X['gender'] = X['gender'].map({'Мужской': 0, 'Женский': 1})
        X['category'] = X['category'].astype('category').cat.codes
        X['subcategory'] = X['subcategory'].astype('category').cat.codes

        # Преобразование векторных представлений в отдельные признаки
        user_embedding_features = pd.DataFrame(X['user_embedding'].tolist(), index=X.index)
        user_embedding_features = user_embedding_features.add_prefix('user_embedding_')

        place_embedding_features = pd.DataFrame(X['place_embedding'].tolist(), index=X.index)
        place_embedding_features = place_embedding_features.add_prefix('place_embedding_')

        # Объединение всех признаков
        X = pd.concat([X[['age', 'gender', 'category', 'subcategory']], user_embedding_features, place_embedding_features], axis=1)

        # Обучение модели CatBoost
        model = train_catboost(X, y)

        # Сохранение моделей
        with open('svd_model.pkl', 'wb') as file:
            pickle.dump((user_embeddings, place_embeddings), file)

        model.save_model('catboost_model.pkl')

        # Обновление даты последнего обновления модели
        last_update_date = current_date

        print("Модель успешно переобучена!")
    else:
        print("Модель еще актуальна. Переобучение не требуется.")

# Пример использования
retrain_model()