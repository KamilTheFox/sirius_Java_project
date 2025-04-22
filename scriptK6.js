import http from 'k6/http';
import { check, sleep } from 'k6';

// Счетчики для статистики
let statusZeroCount = 0;
let postRequests = 0;
let getRequests = 0;

export function setup() {
    const allRestaurants = http.get('http://localhost:8080/restaurants').json();
    // Берем только первые 10 случайных ресторанов
    const shuffled = [...allRestaurants].sort(() => 0.5 - Math.random()).slice(0, 10);
    return { restaurants: shuffled };
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

function makePostRequest() {
    const names = ["Pasta Place", "Burger Joint", "Sushi Spot", "Taco Stand", "Pizza Heaven"];
    const cuisines = ["Italian", "American", "Japanese", "Mexican", "French"];

    const payload = {
        name: names[Math.floor(Math.random() * names.length)],
        cuisine: cuisines[Math.floor(Math.random() * cuisines.length)],
        minimumOrder: Math.random() * 50 + 10 // Случайное значение от 10 до 60
    };

    const res = http.post(
        'http://localhost:8080/restaurants',
        JSON.stringify(payload),
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { type: 'POST' }
        }
    );

    if (res.status === 0) statusZeroCount++;
    postRequests++;

    check(res, {
        'Restaurant created (status 200)': (r) => r.status === 200,
        'Response has restaurant identifier': (r) => !!r.json('identifier')
    });

    if (res.status !== 200 && res.status !== 0) {
        console.error(`POST failed. Status: ${res.status}, Body: ${res.body}`);
    }
}

function makeGetRequest(data) {
    if (data.restaurants.length === 0) return;

    const restaurant = data.restaurants[Math.floor(Math.random() * data.restaurants.length)];
    const res = http.get(
        `http://localhost:8080/orders/restaurant/${restaurant.identifier}/average-check`,
        { tags: { type: 'GET' } }
    );

    if (res.status === 0) statusZeroCount++;
    getRequests++;

    check(res, {
        'GET successful (status 200)': (r) => r.status === 200,
        'Response has data': (r) => r.body.length > 0
    });

    if (res.status !== 200 && res.status !== 0) {
        console.error(`GET failed. Status: ${res.status}`);
    }
}

export default function (data) {
    const ratio = __ENV.RATIO || '50/50';
    const [postRatio, getRatio] = ratio.split('/').map(Number);
    const random = Math.random() * 100;

    if (random < postRatio) {
        makePostRequest();
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