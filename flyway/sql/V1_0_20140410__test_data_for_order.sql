-- data for Integration tests
alter table "order" add column currency_id bigint NOT NULL;
alter table "order_position" drop column currency_id;

INSERT INTO base_user (id, name, email, password) VALUES (100, 'Поставщик 2', 'seller2@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (100, 1, '4771044d44ef4242a0d84ae095f6f3d0', TRUE);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1, 3, 2, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (2, 3, 2, 2, '2014-03-01', 'CONFIRMED', '+375291234567', 'ул. Орловская1', 'пётр петров', false,true,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (3, 3, 2, 3, '2014-03-01', 'SENT', '+375291234567', 'ул. Орловская2', 'пётр петров', false,false,true, '10:00', '11:00', '2014-03-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (4, 3, 100, 1, '2014-03-01', 'CANCELED', '+375291234567', 'ул. Орловская3', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (5, 3, 100, 2, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская4', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (6, 3, 2, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'пётр петров', true,false,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (7, 3, 2, 2, '2014-03-01', 'CONFIRMED', '+375291234567', 'ул. Орловская1', 'пётр петров', false,true,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (8, 3, 2, 3, '2014-03-01', 'SENT', '+375291234567', 'ул. Орловская2', 'пётр петров', false,false,true, '10:00', '11:00', '2014-02-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (9, 3, 100, 1, '2014-03-01', 'CANCELED', '+375291234567', 'ул. Орловская3', 'пётр петров', true,false,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (10, 3, 100, 2, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская4', 'пётр петров', true,false,false, '10:00', '11:00', '2014-02-03', 5000, 200, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1, 1, 1, 1, 100, 20, 5, 999, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (2, 1, 1, 2, 100, 20, 5, 999, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (3, 2, 2, 2, 110, 11, 10, 1000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (4, 2, 2, 3, 110, 11, 10, 1000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (5, 3, 3, 3, 120, 8, 15, 1001, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (6, 3, 3, 4, 120, 8, 15, 1001, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (7, 4, 4, 4, 250, 10, 25, 1002, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (8, 4, 4, 5, 250, 10, 25, 1002, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (9, 5, 5, 5, 135, 3, 45, 1003, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (10, 5, 5, 1, 135, 3, 45, 1003, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (11, 6, 1, 1, 100, 20, 5, 999, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (12, 6, 1, 2, 100, 20, 5, 999, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (13, 7, 2, 2, 110, 11, 10, 1000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (14, 7, 2, 3, 110, 11, 10, 1000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (15, 8, 3, 3, 120, 8, 15, 1001, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (16, 8, 3, 4, 120, 8, 15, 1001, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (17, 9, 4, 4, 250, 10, 25, 1002, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (18, 9, 4, 5, 250, 10, 25, 1002, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (19, 10, 5, 5, 135, 3, 45, 1003, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (20, 10, 5, 1, 135, 3, 45, 1003, 1);
