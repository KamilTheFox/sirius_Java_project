import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

// Функция для безопасного получения данных
function fetchWithFallback(url, fallback) {
    try {
        const res = http.get(url);
        if (res.status === 200) {
            return JSON.parse(res.body);
        }
    } catch (e) {
        console.error(`Error fetching ${url}:`, e);
    }
    return fallback;
}

const initData = new SharedArray('init_data', function() {
    const restaurants = fetchWithFallback('http://localhost:8080/restaurants', []);

    if (restaurants.length === 0) {
        console.error('No restaurants found! Please run data generator first');
        return [];
    }

    return restaurants.map(rest => {
        const dishes = fetchWithFallback(
            `http://localhost:8080/dishes/restaurant/${rest.identifier}`,
            []
        );
        return {
            restaurantId: rest.identifier,
            dishes: dishes.map(d => d.identifier)
        };
    }).filter(data => data.dishes.length > 0); // Отфильтровываем рестораны без блюд
});

export const options = {
    stages: [
        { duration: '30s', target: 100 },  // Разгон
        { duration: '1m', target: 100 },   // Постоянная нагрузка
        { duration: '30s', target: 0 },    // Завершение
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.05'],    // Допустимо менее 5% ошибок
    },
};

function shouldPerformGet(ratio) {
    const random = Math.random();
    switch (ratio) {
        case '5/95': return random > 0.05;
        case '50/50': return random > 0.5;
        case '95/5': return random > 0.95;
        default: return random > 0.5;
    }
}

export default function () {
    const ratio = __ENV.RATIO || '50/50';

    if (initData.length === 0) {
        console.error('No test data available!');
        return;
    }

    if (shouldPerformGet(ratio)) {
        // GET запрос
        const res = http.get('http://localhost:8080/restaurants');
        check(res, {
            'GET status was 200': (r) => r.status === 200,
            'GET has data': (r) => JSON.parse(r.body).length > 0
        });
    } else {
        // POST запрос
        const restaurantData = initData[Math.floor(Math.random() * initData.length)];
        const dishesCount = Math.max(1, Math.floor(Math.random() * 5));

        const selectedDishes = [];
        for (let i = 0; i < dishesCount && i < restaurantData.dishes.length; i++) {
            selectedDishes.push(
                restaurantData.dishes[Math.floor(Math.random() * restaurantData.dishes.length)]
            );
        }

        const payload = JSON.stringify({
            restaurant: restaurantData.restaurantId,
            dishes: selectedDishes,
            deliveryAddress: "Test Address, 123"
        });

        const params = {
            headers: { 'Content-Type': 'application/json' },
            timeout: '30s'
        };

        const res = http.post('http://localhost:8080/orders', payload, params);

        check(res, {
            'POST status was 201': (r) => r.status === 201,
            'POST response time < 500ms': (r) => r.timings.duration < 500
        });
    }

    sleep(0.1); // Пауза между запросами
}