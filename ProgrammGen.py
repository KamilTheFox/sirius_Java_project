import argparse
import requests
from faker import Faker
import uuid
import random

fake = Faker()

class DataGenerator:
    def __init__(self, base_url):
        self.base_url = base_url

    def clear_data(self, endpoint):
        response = requests.delete(f"{self.base_url}/{endpoint}/clear")
        print(f"Cleared {endpoint} data. Status: {response.status_code}")

    def create_restaurant(self):
        data = {
            "name": fake.company(),
            "cuisine": random.choice(["Итальянская", "Японская", "Русская", "Французская"]),
            "minimumOrder": round(random.uniform(500, 2000), 2)
        }
        response = requests.post(f"{self.base_url}/restaurants", json=data)
        return response.json()

    def create_dish(self, restaurant_id):
        data = {
            "name": fake.food(),
            "price": round(random.uniform(100, 1000), 2),
            "weight": round(random.uniform(100, 1000), 2),
            "restaurantId": restaurant_id
        }
        response = requests.post(f"{self.base_url}/dishes", json=data)
        return response.json()

    def create_order(self, restaurant_id, dish_ids):
        data = {
            "restaurant": restaurant_id,
            "dishes": random.sample(dish_ids, random.randint(1, min(5, len(dish_ids)))),
            "deliveryAddress": fake.address()
        }
        response = requests.post(f"{self.base_url}/orders", json=data)
        return response.json()

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

    # Если указан флаг --delete, только очищаем данные и завершаем работу
    if args.delete:
        generator.clear_data(args.endpoint)
        return

    # Очищаем данные если указан флаг --clear
    if args.clear:
        generator.clear_data(args.endpoint)

    if args.endpoint == "restaurants":
        for _ in range(args.count):
            generator.create_restaurant()

    elif args.endpoint == "dishes":
        restaurants = [generator.create_restaurant() for _ in range(10)]
        restaurant_ids = [rest['id'] for rest in restaurants]

        for _ in range(args.count):
            generator.create_dish(random.choice(restaurant_ids))

    elif args.endpoint == "orders":
        restaurants = [generator.create_restaurant() for _ in range(5)]
        restaurant_ids = [rest['id'] for rest in restaurants]

        dishes_by_restaurant = {}
        for rest_id in restaurant_ids:
            dishes = [generator.create_dish(rest_id) for _ in range(10)]
            dishes_by_restaurant[rest_id] = [dish['id'] for dish in dishes]

        for _ in range(args.count):
            rest_id = random.choice(restaurant_ids)
            generator.create_order(rest_id, dishes_by_restaurant[rest_id])

if __name__ == "__main__":
    main()