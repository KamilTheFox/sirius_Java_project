# Этап сборки
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

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app

# Копируем jar из первого этапа
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]