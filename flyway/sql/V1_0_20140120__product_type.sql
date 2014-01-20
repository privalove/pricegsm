CREATE TABLE product_type (
  id          BIGINT,
  name        VARCHAR(255) NOT NULL,
  yandex_id   BIGINT       NOT NULL,
  active      BOOLEAN      NOT NULL DEFAULT TRUE,

  modified_by BIGINT       NOT NULL DEFAULT 0,
  modified    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT product_type_pkey PRIMARY KEY (id),
  CONSTRAINT product_type_name_unique UNIQUE (name)
);
ALTER TABLE product_type OWNER TO pricegsmowner;

CREATE SEQUENCE product_type_seq START 4;
ALTER TABLE product_type_seq OWNER TO pricegsmowner;

INSERT INTO product_type (id, name, yandex_id) VALUES (1, 'Мобильный телефон', 91491);
INSERT INTO product_type (id, name, yandex_id) VALUES (2, 'Планшет', 6427100);
INSERT INTO product_type (id, name, yandex_id) VALUES (3, 'Ноутбук', 91013);

ALTER TABLE product ADD COLUMN product_type_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE product ADD CONSTRAINT product_product_type_fkey FOREIGN KEY (product_type_id) REFERENCES product_type (id);

ALTER TABLE product DROP COLUMN yandex_type_id;