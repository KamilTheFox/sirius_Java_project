#!/usr/bin/env python3
import uuid
import random
from datetime import datetime, timedelta

def generate_sql():
    # Создание таблиц
    sql = """
    CREATE TABLE IF NOT EXISTS restaurant (
        identifier UUID PRIMARY KEY,
        name VARCHAR(255),
        cuisine VARCHAR(255),
        minimum_order DOUBLE PRECISION
    );

    CREATE TABLE IF NOT EXISTS dish (
        identifier UUID PRIMARY KEY,
        name VARCHAR(255),
        price DOUBLE PRECISION,
        weight DOUBLE PRECISION,
        restaurant UUID REFERENCES restaurant(identifier)
    );

    CREATE TABLE IF NOT EXISTS orders (
        identifier UUID PRIMARY KEY,
        restaurant UUID REFERENCES restaurant(identifier),
        dishes UUID[],
        delivery_address VARCHAR(255),
        delivery_time TIMESTAMP,
        total_amount DOUBLE PRECISION
    );
    """

    # Данные ресторанов
    restaurants = [
        (str(uuid.uuid4()), "Вега", "Вкусная", 45),
        (str(uuid.uuid4()), "Шаурмечная", "Странная", 250),
        (str(uuid.uuid4()), "Элит", "Элитная", 1500),
        (str(uuid.uuid4()), "Сушибар", "Сушенная", 350)
    ]

    # INSERT для ресторанов
    for r in restaurants:
        sql += f"""
        INSERT INTO restaurant (identifier, name, cuisine, minimum_order)
        VALUES ('{r[0]}', '{r[1]}', '{r[2]}', {r[3]});
        """

    # Данные блюд
    dishes = [
        # Ресторан Вега (restaurants[0])
            (str(uuid.uuid4()), "Суп", 100, 100, restaurants[0][0]),
            (str(uuid.uuid4()), "Картофель", 150, 120, restaurants[0][0]),
            (str(uuid.uuid4()), "Говядина", 200, 10, restaurants[0][0]),
            (str(uuid.uuid4()), "Курица", 100, 120, restaurants[0][0]),

            # Шаурмечная (restaurants[1])
            (str(uuid.uuid4()), "Шаурма", 300, 250, restaurants[1][0]),
            (str(uuid.uuid4()), "Шаурма с картофелем", 360, 350, restaurants[1][0]),
            (str(uuid.uuid4()), "Шаурма с чили", 330, 300, restaurants[1][0]),

            # Элит (restaurants[2])
            (str(uuid.uuid4()), "Запеканка", 400, 100, restaurants[2][0]),
            (str(uuid.uuid4()), "Крабы", 400, 200, restaurants[2][0]),
            (str(uuid.uuid4()), "Лобстеры", 500, 150, restaurants[2][0]),
            (str(uuid.uuid4()), "Мидии", 500, 150, restaurants[2][0]),
            (str(uuid.uuid4()), "Лосось", 500, 150, restaurants[2][0]),
            (str(uuid.uuid4()), "Говядина", 500, 150, restaurants[2][0]),
            (str(uuid.uuid4()), "Язык говяжий", 500, 150, restaurants[2][0]),
            (str(uuid.uuid4()), "Глаза", 500, 150, restaurants[2][0]),

            # Сушибар (restaurants[3])
            (str(uuid.uuid4()), "Сет мини", 450, 500, restaurants[3][0]),
            (str(uuid.uuid4()), "Сет Суши", 1200, 1350, restaurants[3][0]),
            (str(uuid.uuid4()), "Сет Дубай", 770, 800, restaurants[3][0]),
            (str(uuid.uuid4()), "Комбо", 650, 650, restaurants[3][0]),
            (str(uuid.uuid4()), "Филадельфия", 1000, 1000, restaurants[3][0])
    ]

    # INSERT для блюд
    for d in dishes:
        sql += f"""
        INSERT INTO dish (identifier, name, price, weight, restaurant)
        VALUES ('{d[0]}', '{d[1]}', {d[2]}, {d[3]}, '{d[4]}');
        """

    # Адреса
    addresses = [
        "пгт Сириус, Олимпийский проспект, д. 15",
        "пгт Сириус, ул. Воскресенская, д. 12",
        "пгт Сириус, Парусный бульвар, д. 12",
        "пгт Сириус, проспект Континентальный, д. 6",
        "пгт Сириус, Триумфальный проезд, д. 9",
        "пгт Сириус, Крымская улица, д. 22",
        "пгт Сириус, Морской бульвар, д. 7",
        "пгт Сириус, Платановая аллея, д. 3",
        "пгт Сириус, Олимпийский парк, д. 11",
        "пгт Сириус, Имеретинская набережная, д. 1"
    ]

    # Генерация заказов
    for _ in range(10):
        order_id = str(uuid.uuid4())
        restaurant = random.choice(restaurants)
        restaurant_dishes = [d for d in dishes if d[4] == restaurant[0]]

        # Выбираем случайные блюда для заказа
        selected_dishes = []
        total_amount = 0
        while total_amount < restaurant[3] and restaurant_dishes:  # minimum_order check
            dish = random.choice(restaurant_dishes)
            selected_dishes.append(dish[0])  # UUID блюда
            total_amount += dish[2]  # price
            restaurant_dishes.remove(dish)

        delivery_time = datetime.now() + timedelta(hours=random.randint(1, 24))
        address = random.choice(addresses)

        dishes_array = '{"' + '","'.join(selected_dishes) + '"}'

        sql += f"""
        INSERT INTO orders (identifier, restaurant, dishes, delivery_address, delivery_time, total_amount)
        VALUES (
            '{order_id}',
            '{restaurant[0]}',
            '{dishes_array}'::uuid[],
            '{address}',
            '{delivery_time}',
            {total_amount}
        );
        """

    return sql

if __name__ == "__main__":
    # Генерируем SQL и сохраняем в файл
    with open('init.sql', 'w', encoding='utf-8') as f:
        f.write(generate_sql())
    print("SQL file generated successfully!")