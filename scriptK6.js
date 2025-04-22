import http from 'k6/http';
import { check, sleep } from 'k6';

// Счетчик для запросов со статусом 0
let statusZeroCount = 0;

export function setup() {
    const restaurants = http.get('http://localhost:8080/restaurants').json();
    const restaurantData = [];

    for (const rest of restaurants) {
        const dishes = http.get(`http://localhost:8080/dishes/restaurant/${rest.identifier}`).json();
        if (dishes.length > 0) {
            restaurantData.push({
                id: rest.identifier,
                dishes: dishes.map(d => ({ id: d.identifier, price: d.price })),
                minOrder: rest.minimumOrder
            });
        }
    }
    return { restaurantData };
}

export const options = {
    setupTimeout: '30s',
    stages: [
        { duration: '10s', target: 10 },
        { duration: '1m', target: 1500 },
        { duration: '5s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.05'],
    },
};

export default function (data) {
    const restaurant = data.restaurantData[Math.floor(Math.random() * data.restaurantData.length)];

    // 1. Collect dishes until we reach the minimum order amount
    let total = 0;
    const selectedDishes = [];
    const shuffledDishes = [...restaurant.dishes].sort(() => 0.5 - Math.random());

    for (const dish of shuffledDishes) {
        if (total >= restaurant.minOrder) break;
        selectedDishes.push(dish.id);
        total += dish.price;
    }

    // 2. If the total is still less than minimum, add the cheapest dish
    if (total < restaurant.minOrder) {
        const cheapestDish = [...restaurant.dishes].sort((a, b) => a.price - b.price)[0];
        selectedDishes.push(cheapestDish.id);
        total += cheapestDish.price;
    }

    // 3. Send the order
    const payload = {
        restaurant: restaurant.id,
        dishes: selectedDishes,
        deliveryAddress: "Test Address, 123"
    };

    const res = http.post(
        'http://localhost:8080/orders',
        JSON.stringify(payload),
        { headers: { 'Content-Type': 'application/json' } }
    );

    // Считаем запросы со статусом 0, но не логируем их
    if (res.status === 0) {
        statusZeroCount++;
    }

    // 4. Check the response
    const checkResult = check(res, {
        'Order created (status 200)': (r) => r.status === 200,
        'Response has order identifier': (r) => {
            try {
                const response = JSON.parse(r.body);
                return !!response.identifier;
            } catch {
                return false;
            }
        }
    });

    // Логируем только реальные ошибки (не 200 и не 0)
    if (res.status !== 200 && res.status !== 0) {
        console.error(`Request failed. Status: ${res.status}, Body: ${res.body}`);
        console.log('Sent payload:', JSON.stringify(payload, null, 2));
        if (res.status === 400 && res.body.includes("minimumOrder")) {
            console.log(`Minimum order: ${restaurant.minOrder}, current total: ${total}`);
        }
    }

    sleep(0.5);
}

// Выводим итоговое количество запросов со статусом 0
export function teardown() {
    if (statusZeroCount > 0) {
        console.log(`\n[Итог] Всего запросов со статусом 0: ${statusZeroCount}`);
    }
}