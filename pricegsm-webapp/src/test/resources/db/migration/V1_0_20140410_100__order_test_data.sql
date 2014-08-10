-- data for Integration tests

INSERT INTO base_user (id, name, email, password)
VALUES (10000, 'Поставщик 2', 'testSeller2@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

INSERT INTO pricegsm_user (id, region_id, token, email_valid)
VALUES (10000, 1, '4781044d44ef4242a0d84ae095f6f3d0', TRUE);

INSERT INTO base_user (id, name, email, password)
VALUES (10001, 'Поставщик 2', 'testBuyer2@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

INSERT INTO pricegsm_user (id, region_id, token, email_valid)
VALUES (10001, 1, '4791044d44ef4242a0d84ae095f6f3d0', TRUE);

insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version)
values (10000, 99, 1, '2014-03-01', '2015-03-01', 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (9900, 10000, 99, 1, 1, 566, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (10000, 10000, 99, 2, 2, 566, 999, 2, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1001, 10001, 2, 0, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1002, 10001, 2, 0, 2, '2014-03-01', 'CONFIRMED', '+375291234567', 'ул. Орловская1', 'пётр петров', false,true,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1001, 1001, 1, 4, 100, 20, 5, 9900, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1002, 1001, 2, 6, 100, 20, 5, 10100, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1003, 1002, 1, 4, 110, 11, 10, 10000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1004, 1002, 2, 5, 110, 11, 10, 10100, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1003, 10001, 2, 0, 2, '2014-03-01', 'CONFIRMED', '+37529000111222', 'street', 'john', false,false,false, '10:00', '11:00', '2014-03-01', 220, 20, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1005, 1003, 1, 4, 110, 11, 10, 10000, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1006, 1003, 2, 5, 110, 11, 10, 10100, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1004, 10001, 2, 0, 2, '2014-03-01', 'CONFIRMED', '+37529000111222', 'street', 'john', false,false,false, '10:00', '11:00', '2014-03-01', 220, 20, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1007, 1004, 1, 4, 110, 11, 10, 10000, 1);
