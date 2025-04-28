import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';


export function setup() {
    return {};
}

export const options = {
    setupTimeout: '30s',
    httpTimeout: '2s',
    consoleOutput: 'none',

    stages: [
        { duration: '10s', target: 10 },
        { duration: '1m', target: 200 },
        { duration: '5s', target: 0 },
    ],
    thresholds: {
        'http_req_duration{type:POST}': ['p(95)<500'],
        'http_req_duration{type:GET}': ['p(95)<300'],
        'http_req_failed': ['rate<0.05'],
    },
};

const postCounter = new Counter('post_requests');
const getCounter = new Counter('get_requests');

function makePostRequest() {
    const names = ["Pasta Place", "Burger Joint", "Sushi Spot", "Taco Stand", "Pizza Heaven"];
    const cuisines = ["Italian", "American", "Japanese", "Mexican", "French"];

    const payload = {
        name: names[Math.floor(Math.random() * names.length)],
        cuisine: cuisines[Math.floor(Math.random() * cuisines.length)],
        minimumOrder: Math.random() * 50 + 10
    };

    const res = http.post(
        'http://10.60.3.17:8080/restaurants',
        JSON.stringify(payload),
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { type: 'POST' }
        }
    );
    postCounter.add(1);
}

function makeGetRequest() {
    try {
        const res = http.get(
            'http://10.60.3.17:8080/orders/restaurants/average-check',
            { tags: { type: 'GET' },timeout: '100s' }
        );
        getCounter.add(1);
    } catch (error) {
    }
}

export default function (data) {
    const ratio = __ENV.RATIO || '50/50';

    const [postRatio, getRatio] = ratio.split('/').map(Number);

    const random = Math.random() * 100;

    if (random < postRatio) {
        makePostRequest();
    } else {
        makeGetRequest();
    }

    sleep(0.5);
}
