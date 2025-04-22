import matplotlib.pyplot as plt

# Данные для трех разных соотношений POST/GET
cpu_limits = [0.5, 1, 1.5, 2]

# Пример данных - замените на свои реальные значения
avg_req_duration_5_95 = [2700, 4001, 1730, 754]    # 5% POST / 95% GET
avg_req_duration_50_50 = [3200, 3800, 1600, 800]   # 50% POST / 50% GET
avg_req_duration_95_5 = [3500, 4200, 1800, 900]    # 95% POST / 5% GET

plt.figure(figsize=(10, 6))

# Рисуем три линии с разными цветами и метками
plt.plot(cpu_limits, avg_req_duration_5_95, marker='o', linestyle='-', color='b', label='5% POST / 95% GET')
plt.plot(cpu_limits, avg_req_duration_50_50, marker='s', linestyle='--', color='r', label='50% POST / 50% GET')
plt.plot(cpu_limits, avg_req_duration_95_5, marker='^', linestyle='-.', color='g', label='95% POST / 5% GET')

plt.xlabel('CPU Limit')
plt.ylabel('Average Request Duration (ms)')
plt.title('Average Request Duration vs. CPU Limit for Different POST/GET Ratios')

plt.grid(True)
plt.legend()  # Добавляем легенду для различия линий

plt.show()