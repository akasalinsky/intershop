# Витрина интернет-магазина (v2.0 - Reactive Stack)

Простой веб-магазин на Spring Boot с корзиной, заказами и пагинацией, переписанный на **реактивный стек (WebFlux, R2DBC)**.

## 🛠 Технологии

*   **Java 21**
*   **Spring Boot 3.5.3**
*   **Spring WebFlux** (реактивный веб-стек)
*   **Thymeleaf** (для серверного рендеринга HTML)
*   **Spring Data R2DBC** (реактивный доступ к данным)
*   **PostgreSQL** (через R2DBC драйвер)
*   **Redis** (для кэширования товаров)
*   **Maven** (система сборки)
*   **Docker** (контейнеризация)
*   **OpenAPI Generator** (генерация клиент-серверного кода)

## 📦 Сборка и запуск

### Локально

#### Требования

*   Java 21 JDK
*   Maven 3.9+
*   Docker (для запуска PostgreSQL и Redis)
*   PostgreSQL (если запускается без Docker, должен быть доступен)
*   Redis (если запускается без Docker, должен быть доступен)

#### 1. Запустить зависимости (PostgreSQL, Redis) через Docker


# Создать и запустить контейнеры с PostgreSQL и Redis
docker run --name intershop-db -e POSTGRES_DB=intershop -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=yourpassword -p 5432:5432 -d postgres:16
docker run --name intershop-redis -p 6379:6379 -d redis:latest

2. Собрать проект

# Из корня проекта (папка intershop)
mvn clean package

3. Запустить основное веб-приложение

# Из корня проекта (папка intershop)
mvn spring-boot:run -pl eshop-web
Приложение будет доступно по адресу: http://localhost:8080

4. (Опционально) Запустить сервис платежей

# Из корня проекта (папка intershop)
mvn spring-boot:run -pl eshop-payment-service
Сервис платежей будет доступен по адресу: http://localhost:8081