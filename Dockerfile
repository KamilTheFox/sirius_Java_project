FROM gradle:latest AS builder

WORKDIR /app

# Копируем сначала только файлы для зависимостей
COPY settings.gradle build.gradle ./
COPY gradle gradle
COPY gradlew ./

# Загружаем зависимости отдельно (это улучшит кэширование)
RUN gradle dependencies --no-daemon

# Теперь копируем исходный код
COPY src src

# Собираем проект
RUN gradle build -x test --no-daemon --warning-mode all

# Второй этап
FROM openjdk:21-jdk-slim

WORKDIR /app

# Копируем jar из первого этапа
COPY --from=builder /app/build/libs/*.jar app.jar

# Добавляем wait-for-it скрипт
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

# Используем wait-for-it для ожидания готовности базы данных
ENTRYPOINT ["/wait-for-it.sh", "database:5432", "--timeout=30", "--strict", "--", "java", "-jar", "app.jar"]