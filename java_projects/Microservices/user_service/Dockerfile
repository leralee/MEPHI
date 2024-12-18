# Используем Maven для сборки
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
# Копируем весь проект
COPY . .
# Переходим в папку user_service и собираем только этот модуль
WORKDIR /app/user_service
RUN mvn clean package -DskipTests

# Создаём лёгкий образ для запуска
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/user_service/target/user_service-1.0-SNAPSHOT.jar user_service.jar
EXPOSE 5001
ENTRYPOINT ["java", "-jar", "user_service.jar"]