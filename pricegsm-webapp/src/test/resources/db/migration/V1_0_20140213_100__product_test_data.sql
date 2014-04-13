-- data for Integration tests
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (100, 'product 1', '104954561', 1, 3);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (101, 'product 2', '10495457', 1, 4);

INSERT INTO vendor (id, name, short_name) VALUES (100, 'vendor', 'App');

INSERT INTO product_type (id, name, yandex_id) VALUES (200, 'product_type 1', 91491);

INSERT INTO color (id, name, yandex_color, code) VALUES (200, 'Test Color 1', 'test', '#e8ed0a');
INSERT INTO color (id, name, yandex_color, code) VALUES (201, 'Test Color 2', 'test', '#e8ed0a');

INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (102, 'product 3', '10495458', 100, 200, 200, 'description', 'search_query', 'exclude_query', 'colorQuery1');
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (1021, 'product 3', '10495458', 100, 201, 200, 'description', 'search_query', 'exclude_query', 'colorQuery2');

INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (1022, 'product 3', '20495459', 100, 200, 200, 'description', 'search_query', 'exclude_query', 'colorQuery1');
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (1023, 'product 3', '20495459', 100, 201, 200, 'description', 'search_query', 'exclude_query', 'colorQuery2');

INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (1024, 'product 3', '20495460', 100, 200, 200, 'description', 'search_query', 'exclude_query', 'colorQuery1');
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id, description, search_query, exclude_query, colorQuery) VALUES (1025, 'product 3', '20495460', 100, 201, 200, 'description', 'search_query', 'exclude_query', 'colorQuery2');


INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (103, 'product 31', '20495458', 100, 5);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (104, 'product 31', '20495458', 100, 5);

-- data for DAO tests
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (200, 'product 4', '10495459', 1, 5);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (201, 'product 4', '10495459', 1, 5);


INSERT INTO product_type (id, name, yandex_id) VALUES (201, 'product_type 2', 6427100);

INSERT INTO vendor (id, name, short_name) VALUES (200, 'vendor 1', 'Apple 1');

INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id) VALUES (203, 'product 7', '10495460', 200, 201, 201);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id) VALUES (204, 'product 7', '10495460', 200, 200, 201);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id) VALUES (205, 'product 6', '10495460', 200, 200, 201);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id, product_type_id) VALUES (206, 'product 9', '10495460', 200, 200, 200);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (207, 'product 6', '10495461', 1, 200);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (208, 'product 9', '10495461', 1, 201);

INSERT INTO vendor (id, name, short_name) VALUES (201, 'vendor 1', 'Apple 1');

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (209, 'product 6', '10495461', 201, 200);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (210, 'product 9', '10495461', 201, 201);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (211, 'product 6', '10495462', 1, 200);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (212, 'product 9', '10495462', 1, 201);

