-- eshop-web/src/main/resources/schema.sql
-- Скрипт для создания таблиц в PostgreSQL

-- Удаляем таблицы, если они уже существуют (для чистоты экспериментов)
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
-- DROP TABLE IF EXISTS users CASCADE; -- Если у тебя будет таблица пользователей

-- Создаем таблицу продуктов
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY, -- BIGSERIAL = BIGINT + AUTO_INCREMENT
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL, -- Для точных денежных значений
    image_url VARCHAR(512) -- Достаточно длинный URL
);

-- Создаем таблицу заказов
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    total_price NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создаем таблицу элементов заказа
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE, -- Внешний ключ на orders
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT, -- Внешний ключ на products
    quantity INT NOT NULL CHECK (quantity > 0), -- Количество должно быть положительным
    price NUMERIC(19, 2) NOT NULL -- Цена на момент покупки
);