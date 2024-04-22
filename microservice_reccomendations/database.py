import sqlite3

import numpy as np
import pandas as pd
from tqdm import tqdm
import json
import sqlite3
from math import radians, cos, sin, asin, sqrt


def insert_categories_and_subcategories(db_path='places.db', categories_file='categories.csv', subcategories_file='subcategories.csv'):
    conn = sqlite3.connect(db_path)
    categories_df = pd.read_csv(categories_file)
    subcategories_df = pd.read_csv(subcategories_file)

    categories_df.to_sql('Categories', conn, if_exists='append', index=False)
    subcategories_df.to_sql('Subcategories', conn, if_exists='append', index=False)

    conn.close()



def insert_places_from_parquet(file_path, db_path='places.db'):
    df = pd.read_parquet(file_path)
    df['rating'] = df['rating'].replace('Нет данных', np.NAN).astype(float)
    conn = sqlite3.connect(db_path)
    df.to_sql('Places', conn, if_exists='append', index=False)
    conn.close()


def insert_places_from_parquet(file_path, db_path='places.db'):
    """
    Inserts place data from a CSV file into the Places table.
    """
    df = pd.read_parquet(file_path)
    df['rating'] = df['rating'].replace('Нет данных',np.NAN)
    df['rating'] = df['rating'].astype(float)
    conn = sqlite3.connect(db_path)
    df.to_sql('Places', conn, if_exists='append', index=False)
    conn.close()

def insert_users_and_visits_from_parquet(user_file_path, visit_file_path, db_path='places.db'):
    """
    Inserts user data from a CSV file into the Users table and visit data into the Visits table.
    """
    user_df = pd.read_parquet(user_file_path)
    visit_df = pd.read_parquet(visit_file_path)

    conn = sqlite3.connect(db_path)
    # Insert Users
    user_df[['id', 'age', 'gender']].drop_duplicates().to_sql('Users', conn, if_exists='append', index=False)
    print(visit_df.columns)

    # Prepare and Insert Visits - Assuming visit_df has necessary columns including place_name to match with Places
    for _, visit in tqdm(visit_df.iterrows()):
        place_id = conn.execute('SELECT id FROM Places WHERE name = ?', (visit['name'],)).fetchone()[0]
        conn.execute('INSERT INTO Visits (user_id, place_id, user_rating) VALUES (?, ?, ?)',
                     (visit['user_id'], place_id, visit['user_rating']))
    conn.commit()
    conn.close()




def create_tables(db_path='places.db'):
    """
    Creates tables for Users, Places, and Visits in the SQLite database.
    """
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Create Users table
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Users (
        id TEXT PRIMARY KEY,
        age INTEGER,
        gender TEXT
    )''')

    # Create Visits table
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Visits (
        id INTEGER PRIMARY KEY,
        user_id INTEGER,
        place_id INTEGER,
        user_rating INTEGER,
        FOREIGN KEY (user_id) REFERENCES Users(id),
        FOREIGN KEY (place_id) REFERENCES Places(id)
    )''')


    # Создаем таблицу категорий
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Categories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT UNIQUE NOT NULL
    );''')

    # Создаем таблицу подкатегорий
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Subcategories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category_id INTEGER NOT NULL,
        name TEXT UNIQUE NOT NULL,
        FOREIGN KEY (category_id) REFERENCES Categories(id)
    );''')

    # Обновляем таблицу мест
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Places (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category_id INTEGER NOT NULL,
        subcategory_id INTEGER NOT NULL,
        name TEXT NOT NULL,
        address TEXT,
        rating REAL,
        image TEXT,
        description TEXT,
        reviews_count INTEGER,
        pos1 REAL,
        pos2 REAL,
        FOREIGN KEY (category_id) REFERENCES Categories(id),
        FOREIGN KEY (subcategory_id) REFERENCES Subcategories(id)
    );''')


    # Обновляем таблицу предпочтений
    cur.execute('''
    CREATE TABLE IF NOT EXISTS Preferences (
    user_id INTEGER,
    subcategory_id INTEGER,
    mark INTEGER,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (subcategory_id) REFERENCES Subcategories(id)
);''')

    conn.commit()
    conn.close()


def get_user_preferences(user_id, db_path='places.db'):
    """
    Получает предпочтения пользователя из таблицы Preferences.

    :param user_id: Идентификатор пользователя.
    :return: Список словарей, каждый из которых представляет предпочтение пользователя.
    """
    # Подключение к базе данных
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()

    # SQL запрос для извлечения предпочтений пользователя
    query = """
    SELECT subcategory_id, mark FROM Preferences
    WHERE user_id = ?
    """
    cur.execute(query, (user_id,))

    # Извлекаем результаты запроса
    preferences = []
    rows = cur.fetchall()
    for row in rows:
        preference = {
            'subcategory_id': row[0],
            'mark': row[1]
        }
        preferences.append(preference)

    # Закрытие соединения с базой данных
    cur.close()
    conn.close()

    return preferences


def get_places_by_subcategory(subcategory_id, db_path='places.db'):
    """
    Извлекает список идентификаторов мест из базы данных, соответствующих заданной подкатегории.

    :param subcategory_id: Идентификатор подкатегории.
    :return: Список идентификаторов мест.
    """
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Исправленный SQL запрос с указанием таблицы для каждого столбца
    query = """
    SELECT Places.id FROM Places
    JOIN Subcategories ON Places.subcategory = Subcategories.name
    WHERE Subcategories.id = ?
    """
    cur.execute(query, (subcategory_id,))
    rows = cur.fetchall()
    place_ids = [row[0] for row in rows]  # Извлекаем только идентификаторы мест

    cur.close()
    conn.close()

    return place_ids




def check_user_history(user_id, db_path='places.db'):
    """
    Проверяет наличие истории взаимодействий пользователя в таблице Visits.

    :param user_id: Идентификатор пользователя.
    :return: True, если история существует, иначе False.
    """
    # Соединение с базой данных
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()

    # SQL запрос для проверки наличия записей пользователя
    query = "SELECT 1 FROM Visits WHERE user_id = ? LIMIT 1"
    cur.execute(query, (user_id,))
    result = cur.fetchone()  # Получаем одну запись, если она есть

    # Закрываем соединение с базой данных
    cur.close()
    conn.close()

    return result is not None  # Возвращаем True, если запись найдена, иначе False



def get_categories_with_subcategories(db_path='places.db'):
    # Подключаемся к базе данных
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row  # Для доступа к данным по имени колонки
    cur = conn.cursor()

    # Получаем все категории
    cur.execute("SELECT id, name FROM Categories")
    categories = cur.fetchall()

    # Для каждой категории получаем подкатегории
    categories_list = []
    for category in categories:
        cur.execute("SELECT id, name FROM Subcategories WHERE category_id = ?", (category['id'],))
        subcategories = cur.fetchall()

        # Формируем список подкатегорий
        subcategories_list = [{'id': subcategory['id'], 'name': subcategory['name']} for subcategory in subcategories]

        # Добавляем категорию и ее подкатегории в общий список
        categories_list.append({
            'id': category['id'],
            'name': category['name'],
            'subcategory': subcategories_list
        })

    conn.close()
    return categories_list





def get_user_info(user_id,db_path='places.db'):
    """
    Fetches a random place from the Places table.
    """
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute(f'''
    SELECT * FROM Users 
    WHERE id = '{user_id}'
    ''')
    user_info = cur.fetchone()

    conn.close()

    return user_info if user_info else None


def get_places(db_path='places.db'):
    """
   Fetches place from the Places table.
   """
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    # cur.execute(f'''
    #     SELECT id,
    #     pos1,
    #     pos2,
    #     category,
    #     subcategory
    #     FROM Places
    #     WHERE description != "Нет данных" AND image != "Нет данных"
    #     ''')
    cur.execute(f'''
        SELECT id,
        pos1,
        pos2,
        category,
        subcategory
        FROM Places
        ''')
    places = cur.fetchall()
    conn.close()
    places_list = [dict(place) for place in places]

    return places_list if places_list else None


def get_places_by_ids(ids, db_path='places.db'):
    """
    Fetches places from the Places table by a list of ids.
    :param ids: List of place ids to fetch.
    :param db_path: Path to the SQLite database file.
    :return: List of dictionaries with place information or None if no places found.
    """
    # Проверяем, не пустой ли список ids
    if not ids:
        return None

    # Создаем строку с плейсхолдерами для SQL-запроса
    placeholders = ','.join('?' for _ in ids)

    # Подключаемся к базе данных
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row  # Устанавливаем row_factory для получения данных в виде словаря
    cur = conn.cursor()

    # Выполняем запрос, передавая список ids
    cur.execute(f'''
        SELECT * FROM Places 
        WHERE id IN ({placeholders})
    ''', ids)

    # Получаем все строки
    places = cur.fetchall()
    conn.close()

    # Преобразуем строки в список словарей
    places_list = [dict(place) for place in places]

    return places_list if places_list else None


def save_user_preferences(db_path, user_id, preferences):
        conn = sqlite3.connect(db_path)
        cur = conn.cursor()

        # Удаляем старые предпочтения пользователя
        cur.execute("DELETE FROM Preferences WHERE user_id = ?", (user_id,))

        # Добавляем новые предпочтения
        for pref in preferences:
            print(pref)
            cur.execute("""
                INSERT INTO Preferences (user_id, subcategory_id, mark)
                VALUES (?, ?, ?)
            """, (user_id, pref['subcategory_id'], pref['mark']))

        conn.commit()
        conn.close()
        return True





def update_user_info(db_path, user_id, age, gender):
        conn = sqlite3.connect(db_path)
        cur = conn.cursor()

        # Проверяем, существует ли пользователь
        cur.execute("SELECT id FROM Users WHERE id = ?", (user_id,))
        if cur.fetchone():
            # Если пользователь существует, обновляем его данные
            cur.execute("UPDATE Users SET age = ?, gender = ? WHERE id = ?", (age, gender, user_id))
        else:
            # Если пользователь не найден, добавляем его в таблицу
            cur.execute("INSERT INTO Users (id, age, gender) VALUES (?, ?, ?)", (user_id, age, gender))

        conn.commit()
        conn.close()






def get_places_with_detailed_info(db_path='places.db', num=1):
    """
    Fetches a random place from the Places table and splits the image column by ';'.
    """
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row  # Установка row_factory для получения результатов в виде словаря
    cur = conn.cursor()
    cur.execute(f'''
    SELECT * FROM FeaturesPlaces 
    WHERE description != "Нет данных" AND image != "Нет данных"
    ORDER BY RANDOM() LIMIT {num}
    ''')
    places = cur.fetchall()
    conn.close()

    # Преобразование каждой записи в словарь и разделение строки изображений
    places_list = []
    for place in places:
        place_dict = dict(place)
        # Разделение строки изображений по ';' и обновление значения в словаре
        place_dict['image'] = place_dict['image'].split(';')

        places_list.append(place_dict)

    return places_list if places_list else None







import sqlite3
import csv




def update_places(db_path, places_data):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Преобразование DataFrame в список словарей
    for place in tqdm(places_data.to_dict('records')):
        cur.execute("""
            UPDATE Places
            SET image = ?, description = ?
            WHERE name = ? AND address = ?
        """, (place['image'], place['description'], place['name'], place['address']))

    conn.commit()
    conn.close()

def read_places_data(csv_path):
    with open(csv_path, encoding='utf-8') as csv_file:
        csv_reader = csv.DictReader(csv_file)
        return list(csv_reader)



def add_new_columns(db_path):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Добавляем колонку image, если она еще не существует
    cur.execute("SELECT * FROM pragma_table_info('Places') WHERE name='image'")
    if not cur.fetchone():
        cur.execute("ALTER TABLE Places ADD COLUMN image TEXT")

    # Добавляем колонку description, если она еще не существует
    cur.execute("SELECT * FROM pragma_table_info('Places') WHERE name='description'")
    if not cur.fetchone():
        cur.execute("ALTER TABLE Places ADD COLUMN description TEXT")

    conn.commit()
    conn.close()



import sqlite3

def remove_columns(db_path):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Создаем временную таблицу без колонок image и description
    cur.execute("""
        CREATE TEMPORARY TABLE IF NOT EXISTS Places_backup (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            category TEXT,
            subcategory TEXT,
            name TEXT NOT NULL,
            address TEXT,
            rating REAL,
            reviews_count INTEGER,
            pos1 REAL,
            pos2 REAL
        )
    """)

    # Копируем данные во временную таблицу, исключая колонки image и description
    cur.execute("""
        INSERT INTO Places_backup (id, category, subcategory, name, address, rating, reviews_count, pos1, pos2)
        SELECT id, category, subcategory, name, address, rating, reviews_count, pos1, pos2 FROM Places
    """)

    # Удаляем оригинальную таблицу
    cur.execute("DROP TABLE Places")

    # Пересоздаем оригинальную таблицу без колонок image и description
    cur.execute("""
        CREATE TABLE Places (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            category TEXT,
            subcategory TEXT,
            name TEXT NOT NULL,
            address TEXT,
            rating REAL,
            reviews_count INTEGER,
            pos1 REAL,
            pos2 REAL
        )
    """)

    # Переносим данные обратно в оригинальную таблицу из временной
    cur.execute("""
        INSERT INTO Places (id, category, subcategory, name, address, rating, reviews_count, pos1, pos2)
        SELECT id, category, subcategory, name, address, rating, reviews_count, pos1, pos2 FROM Places_backup
    """)

    # Удаляем временную таблицу
    cur.execute("DROP TABLE Places_backup")

    conn.commit()
    conn.close()


# remove_columns('places.db')

def print_columns(db_path):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Получаем информацию о таблице
    cur.execute("PRAGMA table_info(Places);")
    columns = cur.fetchall()
    print(columns)

    conn.close()







def create_features_places_table(db_path='places.db'):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()
    cur.execute('''
    DROP TABLE FeaturesPlaces;''')

    cur.execute('''
    CREATE TABLE IF NOT EXISTS FeaturesPlaces (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category TEXT,
        subcategory TEXT,
        name TEXT NOT NULL,
        address TEXT,
        rating REAL,
        image TEXT,
        description TEXT,
        reviews_count INTEGER,
        pos1 REAL,
        pos2 REAL
    );''')

    conn.commit()
    conn.close()



def haversine(lon1, lat1, lon2, lat2):
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371
    return c * r

def get_places_and_clusters(pos2_max, pos1_min, pos2_min, pos1_max, cluster_radius=0.5):
    """pos2_max - максимальная широта (верхний левый угол)
pos1_min - минимальная долгота (верхний левый угол)
pos2_min - минимальная широта (нижний правый угол)
pos1_max - максимальная долгота (нижний правый угол)"""

    conn = sqlite3.connect('places.db')
    cur = conn.cursor()

    cur.execute('''
        SELECT id, name, category, pos1, pos2
        FROM Places
        WHERE pos2 BETWEEN ? AND ? AND pos1 BETWEEN ? AND ?
    ''', (pos2_min, pos2_max, pos1_min, pos1_max))

    places = cur.fetchall()
    print(len(places))

    clusters = {}
    result = []

    for place in places:
        place_id, name, category, pos1, pos2 = place

        # Check if the place belongs to any existing cluster
        belongs_to_cluster = False
        for cluster in clusters.values():
            if haversine(pos1, pos2, cluster['pos1'], cluster['pos2']) <= cluster_radius:
                cluster['count'] += 1
                belongs_to_cluster = True
                break

        if not belongs_to_cluster:
            # Check if the place is close to any other non-clustered place
            for other_place in result:
                if 'cluster' not in other_place and haversine(pos1, pos2, other_place['pos1'], other_place['pos2']) <= cluster_radius:
                    cluster_id = f"cluster_{len(clusters)}"
                    clusters[cluster_id] = {
                        'id': cluster_id,
                        'pos1': (pos1 + other_place['pos1']) / 2,
                        'pos2': (pos2 + other_place['pos2']) / 2,
                        'count': 2
                    }
                    result.remove(other_place)
                    belongs_to_cluster = True
                    break

            if not belongs_to_cluster:
                # Add the place as a separate object
                result.append({
                    'id': place_id,
                    'name': name,
                    'category': category,
                    'pos1': pos1,
                    'pos2': pos2
                })

    # Add clusters to the result
    result.extend(clusters.values())

    conn.close()
    return result

def insert_features_places_from_parquet(file_path, db_path='places.db'):
    df = pd.read_parquet(file_path)
    df['rating'] = df['rating'].replace('Нет данных', np.NAN).astype(float)
    conn = sqlite3.connect(db_path)
    df.to_sql('FeaturesPlaces', conn, if_exists='append', index=False)
    conn.close()


def get_all_places(db_path='places.db'):
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    cur.execute('''
    SELECT 
     id,
     age,
     gender
     FROM Users
    ''')
    visits = cur.fetchall()

    conn.commit()
    conn.close()

    return visits





def print_first_five_rows(db_path, column_name, table_name):
    """
    Печатает первые 5 строк заданного столбца из указанной таблицы.

    :param db_path: Путь к файлу базы данных.
    :param column_name: Имя столбца для вывода.
    :param table_name: Имя таблицы, из которой производится выборка.
    """
    # Подключение к базе данных
    conn = sqlite3.connect(db_path)
    cur = conn.cursor()

    # Подготовка SQL запроса
    query = f"SELECT {column_name} FROM {table_name} LIMIT 5;"

    # Выполнение запроса
    cur.execute(query)
    rows = cur.fetchall()

    # Вывод результатов
    for row in rows:
        print(row[0])  # Печатаем значение столбца

    # Закрытие соединения с базой данных
    cur.close()
    conn.close()



def add_user_rating(user_id, place_id, rating):
    """
    Adds a user's rating for a place to the UserRatings table.

    :param user_id: ID of the user.
    :param place_id: ID of the place.
    :param rating: Rating given by the user.
    :return: True if the rating is added successfully, False otherwise.
    """
    try:
        conn = sqlite3.connect('places.db')
        cur = conn.cursor()

        cur.execute('''
            INSERT INTO UserRatings (user_id, place_id, rating)
            VALUES (?, ?, ?)
        ''', (user_id, place_id, rating))

        conn.commit()
        return True
    except sqlite3.Error as e:
        print(f"Error adding user rating: {e}")
        return False
    finally:
        if conn:
            conn.close()

def add_user_view(user_id, place_id):
    """
    Adds a user's view of a place's detailed information to the UserViews table.

    :param user_id: ID of the user.
    :param place_id: ID of the place.
    :return: True if the view is recorded successfully, False otherwise.
    """
    try:
        conn = sqlite3.connect('places.db')
        cur = conn.cursor()

        cur.execute('''
            INSERT INTO UserViews (user_id, place_id)
            VALUES (?, ?)
        ''', (user_id, place_id))

        conn.commit()
        return True
    except sqlite3.Error as e:
        print(f"Error recording user view: {e}")
        return False
    finally:
        if conn:
            conn.close()



# data = get_all_places()
#
# df = pd.DataFrame(data,columns=['id','age','gender'])
# df.to_parquet('users_full.parquet')

# create_features_places_table()
# insert_features_places_from_parquet('full_data.parquet')

# add_new_columns('places.db')
# places_data = pd.read_parquet('updated_places.parquet')
# update_places('places.db',places_data)
# remove_columns('places.db')
# db_path = 'places.db'
# add_new_columns(db_path)
#
# places_data = read_places_data('new_places_data.csv')
# update_places('places.db', places_data)

# create_tables(db_path='places.db')
# insert_categories_and_subcategories(db_path='places.db', categories_file='categories.csv', subcategories_file='subcategories.csv')
# insert_places_from_parquet(file_path='places_data.parquet', db_path='places.db')

#%%
