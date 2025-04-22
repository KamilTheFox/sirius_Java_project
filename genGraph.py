import pandas as pd
import matplotlib.pyplot as plt
import glob
import os

# Настройка стиля графиков
plt.style.use('ggplot')
plt.figure(figsize=(12, 8))

# Словарь для хранения данных
data = {
    '95/5': {'cores': [], 'avg_response': []},
    '50/50': {'cores': [], 'avg_response': []},
    '5/95': {'cores': [], 'avg_response': []}
}

# Обработка всех CSV файлов
for filepath in glob.glob('cpu_*.csv'):
    filename = os.path.basename(filepath)
    try:
        # Парсим название файла для получения параметров
        parts = filename.split('_')
        cores = parts[1].replace(',', '.')  # Заменяем запятую на точку
        ratio = f"{parts[2]}/{parts[3].split('.')[0]}"  # Форматируем соотношение

        # Читаем CSV файл
        df = pd.read_csv(filepath, low_memory=False)

        # Фильтруем только данные о времени отклика
        response_df = df[df['metric_name'] == 'http_req_duration']

        if len(response_df) == 0:
            print(f"В файле {filename} нет данных о времени отклика")
            continue

        # Рассчитываем среднее время отклика
        avg_response = response_df['metric_value'].mean()

        # Сохраняем данные
        data[ratio]['cores'].append(float(cores))
        data[ratio]['avg_response'].append(avg_response)

        print(f"Обработан: {filename} | Ядра: {cores} | Соотношение: {ratio} | Время отклика: {avg_response:.2f} мс")

    except Exception as e:
        print(f"Ошибка при обработке {filename}: {str(e)}")
        continue

# Проверяем, есть ли данные для построения графика
has_data = any(len(data[ratio]['cores']) > 0 for ratio in data)
if not has_data:
    print("\nНет данных для построения графика. Возможные причины:")
    print("- В файлах отсутствуют записи с metric_name='http_req_duration'")
    print("- Не удалось прочитать файлы")
    exit()

# Сортируем данные по количеству ядер для каждого соотношения
for ratio in data:
    if data[ratio]['cores']:
        # Сортируем оба списка одновременно по cores
        sorted_pairs = sorted(zip(data[ratio]['cores'], data[ratio]['avg_response']))
        data[ratio]['cores'], data[ratio]['avg_response'] = zip(*sorted_pairs)

# Строим графики
for ratio, color, label in zip(['95/5', '50/50', '5/95'],
                              ['blue', 'green', 'red'],
                              ['95% POST / 5% GET', '50% POST / 50% GET', '5% POST / 95% GET']):
    if data[ratio]['cores']:
        plt.plot(data[ratio]['cores'],
                data[ratio]['avg_response'],
                marker='o',
                color=color,
                label=label,
                linewidth=2)

# Настройка графика
plt.title('Среднее время отклика HTTP-запросов\nв зависимости от количества ядер и соотношения POST/GET', pad=20)
plt.xlabel('Количество ядер', labelpad=10)
plt.ylabel('Среднее время отклика (мс)', labelpad=10)
plt.xticks([0.5, 1.0, 1.5, 2.0])
plt.grid(True, which='both', linestyle='--', linewidth=0.5)

# Добавляем легенду только если есть данные
if any(data[ratio]['cores'] for ratio in data):
    plt.legend(title='Соотношение запросов')
else:
    print("\nПредупреждение: Нет данных ни для одного соотношения запросов")

plt.tight_layout()

# Сохраняем график
plt.savefig('response_time_comparison.png', dpi=300)
print("\nГрафик сохранён как 'response_time_comparison.png'")
plt.show()