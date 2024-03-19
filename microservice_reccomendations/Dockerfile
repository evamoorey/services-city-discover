# Используйте официальный образ Python как базовый
FROM python:3.9-slim

# Установите рабочий каталог для вашего приложения
WORKDIR /CITY_DISCOVER_APP

# Скопируйте файлы зависимостей и установите зависимости
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Скопируйте исходный код вашего приложения в рабочий каталог
COPY . .

# Укажите переменную окружения для Flask
ENV FLASK_APP=app.py

# Запустите ваше приложение
CMD ["flask", "run", "--host=0.0.0.0"]