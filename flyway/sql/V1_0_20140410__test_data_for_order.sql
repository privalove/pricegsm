-- data for Integration tests
INSERT INTO "order" (
id, buyer_id, seller_id, send_date, status, phone, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1, 2, 3, '2014-03-01', 'PREPARE', '+375291234567', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, send_date, status, phone, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (2, 2, 3, '2014-03-01', 'SENT', '+375291234567', 'пётр петров', false,true,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, send_date, status, phone, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (3, 2, 3, '2014-03-01', 'CANCELED', '+375291234567', 'пётр петров', false,false,true, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, send_date, status, phone, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (4, 2, 3, '2014-03-01', 'CONFIRMED', '+375291234567', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, send_date, status, phone, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (5, 2, 3, '2014-03-01', 'DECLINED', '+375291234567', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, currency_id, price_list_position, version)
VALUES (1, 1, 1, 1, 100, 20, 5, 1, 999, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, currency_id, price_list_position, version)
VALUES (2, 2, 2, 2, 110, 11, 10, 1, 1000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, currency_id, price_list_position, version)
VALUES (3, 3, 3, 3, 120, 8, 15, 1, 1001, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, currency_id, price_list_position, version)
VALUES (4, 4, 4, 4, 250, 10, 25, 1, 1002, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, currency_id, price_list_position, version)
VALUES (5, 5, 5, 5, 135, 3, 45, 1, 1003, 1);
