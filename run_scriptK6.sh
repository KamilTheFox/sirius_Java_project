#!/bin/bash

# Проверка аргумента
if [ -z "$1" ]; then
  echo "Usage: $0 <cpu_value>"
  exit 1
fi

# Запуск теста и сохранение результата
k6 run -e RATIO="5/95" script.js --out csv="k6_results/cpu_$1_5_95.csv"
k6 run -e RATIO="50/50" script.js --out csv="k6_results/cpu_$1_50_50.csv"
k6 run -e RATIO="95/5" script.js --out csv="k6_results/cpu_$1_95_5.csv"

echo "Тесты для CPU=$1 завершены. Результаты в k6_results/"

