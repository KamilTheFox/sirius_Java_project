import argparse
import requests
from faker import Faker
import uuid
import random
import time

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
        self.restaurants_cache = None
        self.dishes_cache = {}

    def clear_data(self, endpoint):
        response = requests.delete(f"{self.base_url}/{endpoint}/clear")
        print(f"{self.base_url}/{endpoint}/clear")
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

    def get_all_restaurants(self):
        if self.restaurants_cache is None:
            try:
                response = requests.get(f"{self.base_url}/restaurants")
                response.raise_for_status()
                self.restaurants_cache = response.json()
            except requests.exceptions.RequestException as e:
                print(f"Ошибка при получении списка ресторанов: {e}")
                return []
        return self.restaurants_cache

    def get_restaurant_dishes(self, restaurant_id):
        if restaurant_id not in self.dishes_cache:
            try:
                response = requests.get(f"{self.base_url}/dishes/restaurant/{restaurant_id}")
                response.raise_for_status()
                self.dishes_cache[restaurant_id] = response.json()
            except requests.exceptions.RequestException as e:
                print(f"Ошибка при получении блюд ресторана {restaurant_id}: {e}")
                return []
        return self.dishes_cache[restaurant_id]


    def create_order(self, restaurant_id, max_retries=15, delay=1):
        for attempt in range(max_retries):
            try:
                restaurant_dishes = self.get_restaurant_dishes(restaurant_id)
                if not restaurant_dishes:
                    print(f"Нет доступных блюд для ресторана {restaurant_id}")
                    return None

                dish_ids = [dish['identifier'] for dish in restaurant_dishes]

                data = {
                    "restaurant": restaurant_id,
                    "dishes": random.choices(dish_ids, k=random.randint(5, max(5, len(dish_ids)))),
                    "deliveryAddress": f"{fake.street_address()}, {fake.city()}"
                }
                response = requests.post(f"{self.base_url}/orders", json=data)
                if response.status_code == 400:
                    print(f"Ошибка в данных запроса: {data}")
                    return None
                response.raise_for_status()
                return response.json()
            except requests.exceptions.RequestException as e:
                if attempt == max_retries - 1:
                    print(f"Все попытки создания заказа исчерпаны. Последняя ошибка: {e}")
                    return None
                print(f"Попытка {attempt + 1} не удалась: {e}. Повторяем через {delay} сек...")
                time.sleep(delay)

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
        # Получаем существующие рестораны
        restaurants = generator.get_all_restaurants()
        if not restaurants:
            print("Не найдено ресторанов в системе")
            return

        restaurant_ids = [rest['identifier'] for rest in restaurants]

        # Создаем блюда для случайных ресторанов
        for _ in range(args.count):
            restaurant_id = random.choice(restaurant_ids)
            generator.create_dish(restaurant_id)

    elif args.endpoint == "orders":
        restaurants = generator.get_all_restaurants()
        if not restaurants:
            print("Не найдено ресторанов в системе")
            return

        restaurant_ids = [rest['identifier'] for rest in restaurants]

        for _ in range(args.count):
            rest_id = random.choice(restaurant_ids)
            generator.create_order(rest_id)


if __name__ == "__main__":
    main()

