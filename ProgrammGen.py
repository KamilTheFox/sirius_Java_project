import argparse
import requests
from faker import Faker
import uuid
import random

fake = Faker('ru_RU')

# Списки данных для генерации
FOOD_ITEMS = [
    "Карбонара", "Цезарь", "Борщ", "Суши Филадельфия", "Пицца Маргарита",
    "Греческий салат", "Лазанья", "Том Ям", "Пельмени", "Стейк Рибай",
    "Паста Болоньезе", "Оливье", "Роллы Калифорния", "Тирамису", "Рамен"
]

RESTAURANT_NAMES = [
    "Вкусная История", "Старый Город", "У Моря", "Золотой Дракон",
    "Итальянский Дворик", "Пармезан", "Сакура", "Русский Двор",
    "Французское Кафе", "Восточный Экспресс"
]

class DataGenerator:
    def __init__(self, base_url):
        self.base_url = base_url

    def clear_data(self, endpoint):
        response = requests.delete(f"{self.base_url}/{endpoint}/clear")
        print(f"Cleared {endpoint} data. Status: {response.status_code}")

    def create_restaurant(self):
        data = {
            "name": random.choice(RESTAURANT_NAMES) + " " + fake.company_suffix(),
            "cuisine": random.choice(["Итальянская", "Японская", "Русская", "Французская"]),
            "minimumOrder": round(random.uniform(500, 2000), 2)
        }
        try:
            response = requests.post(f"{self.base_url}/restaurants", json=data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании ресторана: {e}")
            return None

    def create_dish(self, restaurant_id):
        data = {
            "name": random.choice(FOOD_ITEMS),
            "price": round(random.uniform(100, 1000), 2),
            "weight": round(random.uniform(100, 1000), 2),
            "restaurantId": restaurant_id
        }
        try:
            response = requests.post(f"{self.base_url}/dishes", json=data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании блюда: {e}")
            return None

    def create_order(self, restaurant_id, dish_ids):
        data = {
            "restaurant": restaurant_id,
            "dishes": random.sample(dish_ids, random.randint(1, min(5, len(dish_ids)))),
            "deliveryAddress": f"{fake.street_address()}, {fake.city()}"
        }
        try:
            response = requests.post(f"{self.base_url}/orders", json=data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Ошибка при создании заказа: {e}")
            return None

def main():
    parser = argparse.ArgumentParser(description='Generate test data for REST API')
    parser.add_argument('--count', type=int, default=500, help='Number of objects to create')
    parser.add_argument('--endpoint', type=str, required=True,
                       choices=['restaurants', 'dishes', 'orders'],
                       help='API endpoint to populate')
    parser.add_argument('--base-url', type=str, default='http://localhost:8080',
                       help='Base URL of the API')
    parser.add_argument('--clear', action='store_true',
                       help='Clear all data before generating new ones')
    parser.add_argument('--delete', action='store_true',
                       help='Only delete data without generating new ones')
    args = parser.parse_args()

    generator = DataGenerator(args.base_url)

    if args.delete:
        generator.clear_data(args.endpoint)
        return

    if args.clear:
        generator.clear_data(args.endpoint)

    if args.endpoint == "restaurants":
        for _ in range(args.count):
            generator.create_restaurant()

    elif args.endpoint == "dishes":
        restaurants = [r for r in [generator.create_restaurant() for _ in range(10)] if r is not None]
        restaurant_ids = [rest.get('identifier') for rest in restaurants if rest.get('identifier')]

        if not restaurant_ids:
            print("Не удалось создать рестораны")
            return

        for _ in range(args.count):
            generator.create_dish(random.choice(restaurant_ids))

    elif args.endpoint == "orders":
        restaurants = [r for r in [generator.create_restaurant() for _ in range(5)] if r is not None]
        restaurant_ids = [rest.get('identifier') for rest in restaurants if rest.get('identifier')]

        if not restaurant_ids:
            print("Не удалось создать рестораны")
            return

        dishes_by_restaurant = {}
        for rest_id in restaurant_ids:
            dishes = [d for d in [generator.create_dish(rest_id) for _ in range(10)] if d is not None]
            dishes_by_restaurant[rest_id] = [dish.get('identifier') for dish in dishes if dish.get('identifier')]

        for _ in range(args.count):
            rest_id = random.choice(restaurant_ids)
            if dishes_by_restaurant[rest_id]:
                generator.create_order(rest_id, dishes_by_restaurant[rest_id])

if __name__ == "__main__":
    main()