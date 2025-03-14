FROM gradle:latest

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл сборки и исходный код
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
COPY src src

# Собираем проект
RUN gradle build -x test --no-daemon

# Собираем проект
RUN gradle build -x test --no-daemon --warning-mode all

# Второй этап - только для запуска
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=0 /app/build/libs/*.jar app.jar

EXPOSE 8080
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
