import sqlite3

import numpy as np
import pandas as pd
from tqdm import tqdm
import json


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
    WHERE id != {user_id}
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
    cur.execute(f'''
        SELECT * FROM Places 
        WHERE description != "Нет данных" AND image != "Нет данных"
        ''')
    places = cur.fetchall()
    conn.close()
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
    SELECT * FROM Places 
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

    for place in tqdm(places_data):
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


# db_path = 'places.db'
# add_new_columns(db_path)
#
# places_data = read_places_data('new_places_data.csv')
# update_places('places.db', places_data)

# create_tables(db_path='places.db')
# insert_categories_and_subcategories(db_path='places.db', categories_file='categories.csv', subcategories_file='subcategories.csv')
# insert_places_from_parquet(file_path='places_data.parquet', db_path='places.db')

#%%
