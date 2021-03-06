delete from pricelist_position where pricelist_user_id >= 999;
delete from pricelist where user_id >= 999;
delete from pricegsm_user where id >= 999;
delete from base_user where id >= 999;

INSERT INTO base_user (id, name, email, password) VALUES (999, 'Продавец 1', 'marketplace1@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');
INSERT INTO base_user (id, name, email, password) VALUES (1000, 'Продавец 2', 'marketplace2@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');
INSERT INTO base_user (id, name, email, password) VALUES (1001, 'Продавец 3', 'marketplace3@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (999, 1, 'b64613bb36164c968013b4340a433011', TRUE);
INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (1000, 1, 'b64613bb36164c968013b4340a433012', TRUE);
INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (1001, 1, 'b64613bb36164c968013b4340a433013', TRUE);

insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version) values (999, 0, 1, '2014-03-01', '2015-03-01', 1);
insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version) values (1000, 0, 1, '2014-03-01', '2015-03-01', 1);
insert into pricelist (user_id, position, currency_id, sell_from_date, sell_to_date, version) values (1001, 0, 1, '2014-03-01', '2015-03-01', 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
    values (999, 999, 0, 1, 1, 566, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1000, 999, 0, 1, 2, 566, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1001, 999, 0, 1, 3, 566, 999, 2, 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1002, 1000, 0, 1, 1, 565, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1003, 1000, 0, 1, 2, 565, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1005, 1000, 0, 1, 3, 565, 999, 2, 1);

insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1006, 1001, 0, 1, 1, 564, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1007, 1001, 0, 1, 2, 564, 999, 2, 1);
insert into pricelist_position (id, pricelist_user_id, pricelist_position, product_id, specification_id, price, amount, min_order_quantity, version)
  values (1008, 1001, 0, 1, 3, 564, 999, 2, 1);