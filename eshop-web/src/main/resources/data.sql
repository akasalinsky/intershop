-- eshop-web/src/main/resources/data.sql
-- Скрипт для заполнения таблиц тестовыми данными

-- Вставляем тестовые товары
INSERT INTO products (name, description, price, image_url) VALUES
('Ноутбук Lenovo ThinkPad X1 Carbon', 'Легкий и мощный бизнес-ноутбук с 14-дюймовым экраном и процессором Intel Core i7.', 89999.99, '/images/lenovo_thinkpad_x1_carbon.jpg'),
('Смартфон Apple iPhone 15 Pro Max', 'Флагманский смартфон с экраном Super Retina XDR, процессором A17 Pro и камерой 48 МП.', 129999.99, '/images/iphone_15_pro_max.jpg'),
('Планшет Apple iPad Air', 'Мощный планшет с экраном Liquid Retina, процессором M1 и поддержкой Apple Pencil (2-го поколения).', 64999.99, '/images/ipad_air.jpg'),
('Наушники Sony WH-1000XM5', 'Беспроводные наушники с шумоподавлением и высококачественным звуком.', 29999.99, '/images/sony_wh1000xm5.jpg'),
('Часы Apple Watch Series 9', 'Смарт-часы с экраном Retina, процессором S9 SiP и датчиками здоровья.', 39999.99, '/images/apple_watch_series_9.jpg'),
('Камера Canon EOS R5', 'Профессиональная зеркальная камера с матрицей 45 МП и 8K видео.', 249999.99, '/images/canon_eos_r5.jpg'),
('Игровая приставка PlayStation 5', 'Флагманская игровая приставка Sony с SSD и поддержкой 3D-звука Tempest.', 49999.99, '/images/ps5.jpg'),
('Кофеварка DeLonghi ECAM23.420.SB', 'Автоматическая кофеварка с системой LatteCrema и регулировкой помола.', 44999.99, '/images/delonghi_ecam.jpg'),
('Пылесос Dyson V15 Detect', 'Беспроводной пылесос с лазерной подсветкой и цифровым двигателем.', 39999.99, '/images/dyson_v15.jpg'),
('Смарт-телевизор Samsung QLED 55QN90A', '55-дюймовый телевизор с разрешением 4K, Quantum Dot и Smart TV.', 89999.99, '/images/samsung_qled_55.jpg'),
('Книга "Чистый код" Роберта Мартина', 'Классическая книга о принципах написания качественного кода.', 1499.99, '/images/clean_code.jpg'),
('Фитнес-браслет Xiaomi Mi Band 7', 'Доступный фитнес-трекер с AMOLED-экраном и множеством функций.', 3999.99, '/images/mi_band_7.jpg');

-- Вставляем тестовые заказы
INSERT INTO orders (total_price, created_at) VALUES
(119999.98, '2023-10-26 10:00:00'),
(169999.97, '2023-10-27 15:30:00');

-- Вставляем элементы заказов
INSERT INTO order_item (order_id, product_id, quantity, price) VALUES
-- Заказ 1: Ноутбук (1 шт.) + Наушники (1 шт.)
(1, 1, 1, 89999.99),
(1, 4, 1, 29999.99),
-- Заказ 2: iPhone (1 шт.) + iPad (1 шт.) + Часы (1 шт.)
(2, 2, 1, 129999.99),
(2, 3, 1, 64999.99),
(2, 5, 1, 39999.99);