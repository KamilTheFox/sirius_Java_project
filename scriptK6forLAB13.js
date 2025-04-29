import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';
import kafka from 'k6/x/kafka'   // Нужно установить расширение k6-kafka

// В setup создаем producer
export function setup() {
    const kafkaConfig = {
        brokers: ['hl22.zil:9092'],
        clientID: 'k6-load-test',
        topic: 'var13'
    };
    return { kafkaConfig };
}

export const options = {
    setupTimeout: '30s',
    httpTimeout: '2s',
    consoleOutput: 'none',

    stages: [
        { duration: '10s', target: 10 },
        { duration: '1m', target: 10 },
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

    // Создаем payload для ресторана
    const restaurantPayload = {
        name: names[Math.floor(Math.random() * names.length)],
        cuisine: cuisines[Math.floor(Math.random() * cuisines.length)],
        minimumOrder: Math.random() * 50 + 10
    };

    // Формируем Kafka сообщение
    const kafkaMessage = {
        entity: "RESTAURANT",
        operation: "POST",
        payload: restaurantPayload
    };

    // Отправляем сообщение в Kafka
    const producer = new kafka.Writer(data.kafkaConfig);
    producer.produce({
        topic: data.kafkaConfig.topic,
        value: JSON.stringify(kafkaMessage)
    });
    producer.close();

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