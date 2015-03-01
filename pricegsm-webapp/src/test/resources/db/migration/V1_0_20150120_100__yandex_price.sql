-- data for Integration tests

INSERT INTO vendor (id, name, short_name) VALUES (101, 'vendor', 'App');

INSERT INTO product (
id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery)
VALUES (2001, 'product 1', '30000000', 101, 200, 200, 'description', 'search_query', 'exclude_query', 'colorQuery1');

INSERT INTO product (
id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery)
VALUES (2002, 'product 1', '30000001', 101, 201, 200, 'description', 'search_query', 'exclude_query', 'colorQuery2');


insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (1, 0, '2014-01-30', 2001, 100, 100, 100, 'shop 1', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (2, 1, '2014-01-30', 2001, 101, 101, 101, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (3, 2, '2014-01-30', 2001, 102, 102, 102, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (4, 0, '2014-01-31', 2001, 100, 100, 100, 'shop 1', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (5, 1, '2014-01-31', 2001, 101, 101, 101, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (6, 2, '2014-01-31', 2001, 102, 102, 102, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (11, 0, '2014-01-30', 2002, 100, 100, 100, 'shop 1', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (12, 1, '2014-01-30', 2002, 101, 101, 101, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (13, 2, '2014-01-30', 2002, 102, 102, 102, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (14, 0, '2014-01-31', 2002, 100, 100, 100, 'shop 1', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (15, 1, '2014-01-31', 2002, 101, 101, 101, 'shop 2', 'link', 0, '2015-02-01', 10);

insert into yandex_price (
id, position, sell_date, product_id, price_rub, price_eur, price_usd, shop, link, modified_by, modified, count)
values (16, 2, '2014-01-31', 2002, 102, 102, 102, 'shop 2', 'link', 0, '2015-02-01', 10);