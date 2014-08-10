-- data for Integration tests
alter table "order" add column currency_id bigint NOT NULL;
alter table "order_position" drop column currency_id;

alter table "order" add column price_list_position integer NOT NULL;

INSERT INTO base_user (id, name, email, password) VALUES (100, 'Поставщик 2', 'seller2@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

INSERT INTO pricegsm_user (id, region_id, token, email_valid, seller_pickup, seller_delivery) VALUES (100, 1, '4771044d44ef4242a0d84ae095f6f3d0', TRUE, TRUE, TRUE);

insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version)
values (100, 99, 1, '2014-03-01', '2015-03-01', 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (99, 100, 99, 1, 1, 566, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (100, 100, 99, 2, 2, 566, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (101, 100, 99, 3, 3, 566, 999, 2, 1);

insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version)
values (2, 0, 1, '2014-03-01', '2015-03-01', 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (110, 2, 0, 4, 1, 6, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (111, 2, 0, 5, 2, 10, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (112, 2, 0, 6, 3, 15, 999, 2, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (1, 3, 2, 0, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (2, 3, 2, 0, 2, '2014-03-01', 'CONFIRMED', '+375291234567', 'ул. Орловская1', 'пётр петров', false,true,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (3, 3, 2, 0, 3, '2014-03-01', 'SENT', '+375291234567', 'ул. Орловская2', 'пётр петров', false,false,true, '10:00', '11:00', '2014-03-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (4, 3, 100, 99, 1, '2014-03-01', 'CANCELED', '+375291234567', 'ул. Орловская3', 'пётр петров', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (5, 3, 100, 99, 2, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская4', 'пётр петров', false,false,false, '10:00', '11:00', '2014-03-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (6, 3, 2, 0, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'пётр петров', true,false,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (7, 3, 2, 0, 2, '2014-03-01', 'CONFIRMED', '+375291234567', 'ул. Орловская1', 'пётр петров', false,true,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (8, 3, 2, 0, 3, '2014-03-01', 'SENT', '+375291234567', 'ул. Орловская2', 'пётр петров', false,false,true, '10:00', '11:00', '2014-02-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (9, 3, 100, 99, 1, '2014-03-01', 'CANCELED', '+375291234567', 'ул. Орловская3', 'пётр петров', true,false,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (10, 3, 100, 99, 2, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская4', 'пётр петров', false,false,false, '10:00', '11:00', '2014-02-03', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (11, 3, 2, 0, 2, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская1', 'пётр петров', false,false,false, '10:00', '11:00', '2014-02-01', 5000, 200, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (12, 3, 2, 0, 3, '2014-03-01', 'PREPARE', '+375291234567', 'ул. Орловская2', 'пётр петров', false,false,false, '10:00', '11:00', '2014-02-03', 5000, 200, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (1, 1, 1, 4, 100, 20, 5, 110, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (2, 1, 2, 6, 100, 20, 5, 112, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (3, 2, 1, 4, 110, 11, 10, 110, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (4, 2, 2, 5, 110, 11, 10, 111, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (5, 3, 1, 5, 120, 8, 15, 111, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (6, 3, 2, 6, 120, 8, 15, 112, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (7, 4, 1, 1, 250, 10, 25, 99, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (8, 4, 2, 2, 250, 10, 25, 100, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (9, 5, 1, 1, 135, 3, 45, 99, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (10, 5, 2, 3, 135, 3, 45, 101, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (11, 6, 1, 4, 100, 20, 5, 110, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (12, 6, 2, 5, 100, 20, 5, 111, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (13, 7, 1, 5, 110, 11, 10, 111, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (14, 7, 2, 6, 110, 11, 10, 112, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (15, 8, 1, 4, 120, 8, 15, 110, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (16, 8, 2, 6, 120, 8, 15, 112, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (17, 9, 1, 1, 250, 10, 25, 99, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (18, 9, 2, 2, 250, 10, 25, 100, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (19, 10, 1, 2, 135, 3, 45, 100, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (20, 10, 2, 3, 135, 3, 45, 101, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (21, 11, 1, 5, 110, 11, 10, 111, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (22, 11, 2, 6, 110, 11, 10, 112, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (23, 12, 1, 4, 120, 8, 15, 110, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (24, 12, 2, 6, 120, 8, 15, 112, 1);


