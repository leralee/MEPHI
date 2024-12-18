# Используем Maven для сборки
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем весь проект
COPY . .

# Переходим в папку order_service и собираем только этот модуль
WORKDIR /app/order_service
RUN mvn clean package -DskipTests

# Создаём лёгкий образ для запуска
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/order_service/target/order_service-1.0-SNAPSHOT.jar order_service.jar
EXPOSE 5002
ENTRYPOINT ["java", "-jar", "order_service.jar"]