import http from 'k6/http';
import { check, sleep } from 'k6';

// Счетчики для статистики
let statusZeroCount = 0;
let postRequests = 0;
let getRequests = 0;

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
    http.delete('http://localhost:8080/orders');
    return { restaurantData };
}

export const options = {
    setupTimeout: '30s',
    httpTimeout: '2s',
    consoleOutput: 'none',
    stages: [
        { duration: '10s', target: 10 },
        { duration: '1m', target: 1000 },
        { duration: '5s', target: 0 },
    ],
    thresholds: {
        'http_req_duration{type:POST}': ['p(95)<500'],
        'http_req_duration{type:GET}': ['p(95)<300'],
        'http_req_failed': ['rate<0.05'],
    },
};

function makePostRequest(data) {
    const restaurant = data.restaurantData[Math.floor(Math.random() * data.restaurantData.length)];

    let total = 0;
    const selectedDishes = [];
    const shuffledDishes = [...restaurant.dishes].sort(() => 0.5 - Math.random());

    for (const dish of shuffledDishes) {
        if (total >= restaurant.minOrder) break;
        selectedDishes.push(dish.id);
        total += dish.price;
    }

    if (total < restaurant.minOrder) {
        const cheapestDish = [...restaurant.dishes].sort((a, b) => a.price - b.price)[0];
        selectedDishes.push(cheapestDish.id);
        total += cheapestDish.price;
    }

    const payload = {
        restaurant: restaurant.id,
        dishes: selectedDishes,
        deliveryAddress: "Test Address, 123"
    };

    const res = http.post(
        'http://localhost:8080/orders',
        JSON.stringify(payload),
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { type: 'POST' }
        }
    );

    if (res.status === 0) statusZeroCount++;
    postRequests++;

    check(res, {
        'Order created (status 200)': (r) => r.status === 200,
        'Response has order identifier': (r) => !!r.json('identifier')
    });

    if (res.status !== 200 && res.status !== 0) {
        console.error(`POST failed. Status: ${res.status}, Body: ${res.body}`);
    }
}

function makeGetRequest(data) {
    const restaurant = data.restaurantData[Math.floor(Math.random() * data.restaurantData.length)];

    // Варианты GET-запросов:
    const endpoints = [
        `/dishes/restaurant/${restaurant.id}`,
        `/orders/restaurant/${restaurant.id}`,
        `/orders/restaurant/${restaurant.id}/average-check`,
        `/restaurants`
    ];

    const endpoint = endpoints[Math.floor(Math.random() * endpoints.length)];
    const res = http.get(`http://localhost:8080${endpoint}`, { tags: { type: 'GET' } });

    if (res.status === 0) statusZeroCount++;
    getRequests++;

    check(res, {
        'GET successful (status 200)': (r) => r.status === 200,
        'Response has data': (r) => r.body.length > 0
    });

    if (res.status !== 200 && res.status !== 0) {
        console.error(`GET failed. Status: ${res.status}, Endpoint: ${endpoint}`);
    }
}

export default function (data) {
    const ratio = __ENV.RATIO || '50/50';
    const [postRatio, getRatio] = ratio.split('/').map(Number);
    const random = Math.random() * 100;

    if (random < postRatio) {
        makePostRequest(data);
    } else {
        makeGetRequest(data);
    }

    sleep(0.5);
}

export function teardown() {
    console.log(`\n[Итог] Соотношение запросов: ${__ENV.RATIO}`);
    console.log(`POST запросов: ${postRequests}`);
    console.log(`GET запросов: ${getRequests}`);
    if (statusZeroCount > 0) {
        console.log(`Запросов со статусом 0: ${statusZeroCount}`);
    }
}