insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version)
values (2, 1, 1, '2014-10-28', '2014-11-03', 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (120, 2, 0, 4, 1, 6, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (121, 2, 0, 5, 2, 10, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (122, 2, 0, 6, 3, 15, 999, 2, 1);

INSERT INTO "order" (
id, buyer_id, seller_id, price_list_position, currency_id, send_date, status, phone, place, contact_name, delivery, pickup, free_delivery, from_time, to_time, delivery_date, total_price, total_amount, version)
VALUES (13, 3, 2, 1, 1, '2014-03-01', 'DECLINED', '+375291234567', 'ул. Орловская', 'иван', true,false,false, '10:00', '11:00', '2014-03-01', 5000, 200, 1);


INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (100, 13, 1, 4, 100, 20, 5, 120, 1);

INSERT INTO "order_position" (id, order_id, "position", product_id, total_price, price, amount, price_list_position, version)
VALUES (101, 13, 2, 6, 100, 20, 5, 122, 1);
