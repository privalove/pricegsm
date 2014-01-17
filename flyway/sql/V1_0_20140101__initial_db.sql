CREATE TABLE "currency" (
  "id"          BIGINT,
  "name"        VARCHAR(255) NOT NULL,
  "code"        VARCHAR(255),
  "symbol"      VARCHAR(255),

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "currency_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "currency_name_unique" UNIQUE (name)
);
ALTER TABLE "currency" OWNER TO pricegsmowner;

CREATE SEQUENCE "currency_seq" START 4;
ALTER TABLE "currency_seq" OWNER TO pricegsmowner;

INSERT INTO currency (id, name, code, symbol) VALUES (1, 'Доллар США', 'USD', '$');
INSERT INTO currency (id, name, code, symbol) VALUES (2, 'Евро', 'EUR', '€');
INSERT INTO currency (id, name, code, symbol) VALUES (3, 'Российский рубль', 'RUB', 'р.');

CREATE TABLE "region" (
  "id"          BIGINT       NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "description" TEXT,

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "region_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "region_name_unique" UNIQUE (name)
);
ALTER TABLE "region" OWNER TO pricegsmowner;

CREATE SEQUENCE "region_seq" START 2;
ALTER TABLE "region_seq" OWNER TO pricegsmowner;

INSERT INTO region (id, name) VALUES (1, 'Москва');

CREATE TABLE specification (
  id            BIGINT       NOT NULL,
  name          VARCHAR(255) NOT NULL,
  active        BOOLEAN      NOT NULL DEFAULT TRUE,
  description   TEXT,

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT specification_pkey PRIMARY KEY (id),
  CONSTRAINT specification_name_unique UNIQUE (name)
);
ALTER TABLE specification OWNER TO pricegsmowner;

CREATE SEQUENCE specification_seq START 6;
ALTER TABLE specification_seq OWNER TO pricegsmowner;

INSERT INTO specification (id, name) VALUES (1, 'USA');
INSERT INTO specification (id, name) VALUES (2, 'EURO');
INSERT INTO specification (id, name) VALUES (3, 'ASIA');
INSERT INTO specification (id, name) VALUES (4, 'РСТ');
INSERT INTO specification (id, name) VALUES (5, 'UK');


CREATE TABLE "base_user" (
  "id"          BIGINT       NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "email"       VARCHAR(255) NOT NULL,
  "password"    VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "base_user_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "base_user_email_unique" UNIQUE (email)
);
ALTER TABLE "base_user" OWNER TO pricegsmowner;

CREATE SEQUENCE "base_user_seq" START 4;
ALTER TABLE "base_user_seq" OWNER TO pricegsmowner;

INSERT INTO base_user (id, name, email, password) VALUES (1, 'Администратор', 'admin@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');
INSERT INTO base_user (id, name, email, password) VALUES (2, 'Поставщик', 'seller@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');
INSERT INTO base_user (id, name, email, password) VALUES (3, 'Покупатель', 'buyer@pricegsm.com', '8e56be82bda733fa92b4430a8bcaf6f866e01e80d2934341b4c2a5fe02e4d48cf9ebe3d516a70d27');

CREATE TABLE "pricegsm_user" (
  "id"                    BIGINT         NOT NULL,
  "phone"                 VARCHAR(255),
  "website"               VARCHAR(255),
  "seller_pickup"         BOOLEAN        NOT NULL DEFAULT FALSE,
  "seller_delivery"       BOOLEAN        NOT NULL DEFAULT FALSE,
  "buyer_delivery"        BOOLEAN        NOT NULL DEFAULT FALSE,
  "seller_delivery_place" VARCHAR(255),
  "seller_pickup_place"   VARCHAR(255),
  "seller_delivery_from"  TIME,
  "seller_delivery_to"    TIME,
  "seller_pickup_from"    TIME,
  "seller_pickup_to"      TIME,
  "seller_delivery_free"  BOOLEAN        NOT NULL DEFAULT FALSE,
  "seller_delivery_min"   INTEGER        NOT NULL DEFAULT 1,
  "seller_delivery_paid"  BOOLEAN        NOT NULL DEFAULT FALSE,
  "seller_delivery_cost"  NUMERIC(10, 2) NOT NULL DEFAULT 0,
  "buyer_delivery_place"  VARCHAR(255),
  "buyer_delivery_from"   TIME,
  "buyer_delivery_to"     TIME,
  "region_id"             BIGINT,
  "balance"               NUMERIC(10, 2) NOT NULL DEFAULT 0,
  "token"                 VARCHAR(255)   NOT NULL,
  "email_valid"           BOOLEAN        NOT NULL DEFAULT FALSE,

  CONSTRAINT "user_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "user_to_base_user_fkey" FOREIGN KEY ("id") REFERENCES "base_user" ("id"),
  CONSTRAINT "user_to_region_fkey" FOREIGN KEY ("region_id") REFERENCES "region" ("id"),
  CONSTRAINT "user_token_unique" UNIQUE (token)
);
ALTER TABLE "pricegsm_user" OWNER TO pricegsmowner;

INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (2, 1, 'b64613bb36164c968013b4340a43300e', TRUE);
INSERT INTO pricegsm_user (id, region_id, token, email_valid) VALUES (3, 1, '4771044d44ef4242a0d84ae095f6f3cf', TRUE);

CREATE TABLE administrator (
  "id" BIGINT NOT NULL,
  CONSTRAINT "administrator_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "administrator_to_base_user_fkey" FOREIGN KEY ("id") REFERENCES "base_user" ("id")
);
ALTER TABLE "administrator" OWNER TO pricegsmowner;

INSERT INTO administrator (id) VALUES (1);

CREATE TABLE "color" (
  "id"           BIGINT       NOT NULL,
  "name"         VARCHAR(255) NOT NULL,
  "yandex_color" VARCHAR(255),
  "code"         VARCHAR(255),
  "active"       BOOLEAN      NOT NULL DEFAULT TRUE,
  "description"  TEXT,

  "modified_by"  BIGINT       NOT NULL DEFAULT 0,
  "modified"     TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "color_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "color" OWNER TO pricegsmowner;

CREATE SEQUENCE "color_seq" START 10;
ALTER TABLE "color_seq" OWNER TO pricegsmowner;

INSERT INTO color (id, name, yandex_color) VALUES (1, 'Black', 'black');
INSERT INTO color (id, name, yandex_color) VALUES (2, 'White', 'white');
INSERT INTO color (id, name, yandex_color) VALUES (3, 'Silver', 'silver');
INSERT INTO color (id, name, yandex_color) VALUES (4, 'Space Gray', 'gray');
INSERT INTO color (id, name, yandex_color) VALUES (5, 'Gold', 'gold');

INSERT INTO color (id, name, yandex_color) VALUES (6, 'Green', 'green');
INSERT INTO color (id, name, yandex_color) VALUES (7, 'Pink', 'pink');
INSERT INTO color (id, name, yandex_color) VALUES (8, 'Blue', 'blue');
INSERT INTO color (id, name, yandex_color) VALUES (9, 'Yellow', 'yellow');

CREATE TABLE "vendor" (
  "id"          BIGINT       NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "short_name"  VARCHAR(20)  NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "description" TEXT,

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "vendor_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "vendor" OWNER TO pricegsmowner;

CREATE SEQUENCE "vendor_seq" START 9;
ALTER TABLE "vendor_seq" OWNER TO pricegsmowner;

INSERT INTO vendor (id, name, short_name) VALUES (1, 'Apple IPhone', 'Apple');
INSERT INTO vendor (id, name, short_name) VALUES (2, 'Apple IPad', 'Apple');
INSERT INTO vendor (id, name, short_name) VALUES (3, 'Sony Mobile', 'Sony');
INSERT INTO vendor (id, name, short_name) VALUES (4, 'Sony Tablet', 'Sony');
INSERT INTO vendor (id, name, short_name) VALUES (5, 'Samsung Mobile', 'Samsung');
INSERT INTO vendor (id, name, short_name) VALUES (6, 'Samsung Tablet', 'Samsung');
INSERT INTO vendor (id, name, short_name) VALUES (7, 'Nokia', 'Nokia');
INSERT INTO vendor (id, name, short_name) VALUES (8, 'HTC', 'HTC');


CREATE TABLE "product" (
  "id"             BIGINT       NOT NULL,
  "name"           VARCHAR(255) NOT NULL,
  "yandex_id"      VARCHAR(255) NOT NULL,
  "yandex_type_id" VARCHAR(255) NOT NULL DEFAULT '91491', -- mobile phones
  "vendor_id"      BIGINT       NOT NULL,
  "color_id"       BIGINT       NOT NULL,
  "active"         BOOLEAN      NOT NULL DEFAULT TRUE,
  "description"    TEXT,
  "show_in_stat"   BOOLEAN      NOT NULL DEFAULT TRUE,

  "modified_by"    BIGINT       NOT NULL DEFAULT 0,
  "modified"       TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "product_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "product_to_vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("id"),
  CONSTRAINT "product_to_color_fkey" FOREIGN KEY ("color_id") REFERENCES "color" ("id")
);
ALTER TABLE "product" OWNER TO pricegsmowner;

CREATE SEQUENCE "product_seq" START 10;
ALTER TABLE "product_seq" OWNER TO pricegsmowner;

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (1, 'iPhone 5S 16Gb', '10495456', 1, 3);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (2, 'iPhone 5S 16Gb', '10495456', 1, 4);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (3, 'iPhone 5S 16Gb', '10495456', 1, 5);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (4, 'iPhone 5S 32Gb', '10495486', 1, 3);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (5, 'iPhone 5S 32Gb', '10495486', 1, 4);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (6, 'iPhone 5S 32Gb', '10495486', 1, 5);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (7, 'iPhone 5S 64Gb', '10495487', 1, 3);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (8, 'iPhone 5S 64Gb', '10495487', 1, 4);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (9, 'iPhone 5S 64Gb', '10495487', 1, 5);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (10, 'iPhone 5C 16Gb', '10495457', 1, 2);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (11, 'iPhone 5C 16Gb', '10495457', 1, 6);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (12, 'iPhone 5C 16Gb', '10495457', 1, 7);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (13, 'iPhone 5C 16Gb', '10495457', 1, 8);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (14, 'iPhone 5C 16Gb', '10495457', 1, 9);

INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (15, 'iPhone 5C 32Gb', '10495515', 1, 2);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (16, 'iPhone 5C 32Gb', '10495515', 1, 6);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (17, 'iPhone 5C 32Gb', '10495515', 1, 7);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (18, 'iPhone 5C 32Gb', '10495515', 1, 8);
INSERT INTO product (id, name, yandex_id, vendor_id, color_id) VALUES (19, 'iPhone 5C 32Gb', '10495515', 1, 9);


CREATE TABLE "order" (
  "id"            BIGINT         NOT NULL,
  "buyer_id"      BIGINT         NOT NULL,
  "seller_id"     BIGINT         NOT NULL,
  "send_date"     TIMESTAMP WITH TIME ZONE,
  "status"        VARCHAR(255)   NOT NULL,
  "version"       INTEGER,
  "phone"         VARCHAR(255)   NOT NULL,
  "contact_name"  VARCHAR(255)   NOT NULL,
  "delivery"      BOOLEAN        NOT NULL DEFAULT FALSE,
  "pickup"        BOOLEAN        NOT NULL DEFAULT FALSE,
  "free_delivery" BOOLEAN        NOT NULL DEFAULT FALSE,
  "delivery_cost" NUMERIC(10, 2),
  "place"         VARCHAR(255),
  "from_time"     TIME,
  "to_time"       TIME,
  "delivery_date" DATE,
  "description"   TEXT,
  "total_price"   NUMERIC(10, 2) NOT NULL,
  "total_amount"  INTEGER        NOT NULL,

  "modified_by"   BIGINT         NOT NULL DEFAULT 0,
  "modified"      TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "order_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "order_to_seller_fkey" FOREIGN KEY ("seller_id") REFERENCES "pricegsm_user" ("id"),
  CONSTRAINT "order_to_buyer_fkey" FOREIGN KEY ("buyer_id") REFERENCES "pricegsm_user" ("id")
);
ALTER TABLE "order" OWNER TO pricegsmowner;

CREATE SEQUENCE "order_seq";
ALTER TABLE "order_seq" OWNER TO pricegsmowner;

CREATE TABLE "order_position" (
  "id"          BIGINT         NOT NULL,
  "order_id"    BIGINT         NOT NULL,
  "position"    INTEGER,
  "product_id"  BIGINT         NOT NULL,
  "total_price" NUMERIC(10, 2) NOT NULL,
  "price"       NUMERIC(10, 2) NOT NULL,
  "amount"      INTEGER        NOT NULL,
  "version"     INTEGER,
  "currency_id" BIGINT         NOT NULL,

  "modified_by" BIGINT         NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "order_position_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "order_position_to_order_fkey" FOREIGN KEY ("order_id") REFERENCES "order" ("id") ON DELETE CASCADE,
  CONSTRAINT "order_position_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  CONSTRAINT "order_position_to_currency_fkey" FOREIGN KEY ("currency_id") REFERENCES "currency" ("id")
);
ALTER TABLE "order_position" OWNER TO pricegsmowner;

CREATE SEQUENCE "order_position_seq";
ALTER TABLE "order_position_seq" OWNER TO pricegsmowner;

CREATE TABLE "yandex_price" (
  "id"          BIGINT                   NOT NULL,
  "position"    INTEGER                  NOT NULL,
  "sell_date"   TIMESTAMP WITH TIME ZONE NOT NULL,
  "product_id"  BIGINT                   NOT NULL,
  "price_rub"   NUMERIC(10, 2)           NOT NULL,
  "price_eur"   NUMERIC(10, 2),
  "price_usd"   NUMERIC(10, 2),
  "shop"        VARCHAR(255),
  "link"        TEXT, -- unbelievable long urls from yandex

  "modified_by" BIGINT                   NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "yandex_price_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "yandex_price_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE,
  CONSTRAINT "yandex_price_unique" UNIQUE (shop, position, product_id, sell_date)
);
ALTER TABLE "yandex_price" OWNER TO pricegsmowner;

CREATE SEQUENCE "yandex_price_seq";
ALTER TABLE "yandex_price_seq" OWNER TO pricegsmowner;

CREATE TABLE "world_price" (
  "id"          BIGINT                   NOT NULL,
  "position"    INTEGER                  NOT NULL,
  "sell_date"   TIMESTAMP WITH TIME ZONE NOT NULL,
  "product_id"  BIGINT                   NOT NULL,
  "price_rub"   NUMERIC(10, 2),
  "price_eur"   NUMERIC(10, 2),
  "price_usd"   NUMERIC(10, 2),

  "modified_by" BIGINT                   NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "world_price_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "world_price_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE
);
ALTER TABLE "world_price" OWNER TO pricegsmowner;

CREATE SEQUENCE "world_price_seq";
ALTER TABLE "world_price_seq" OWNER TO pricegsmowner;

CREATE TABLE "pricelist" (
  "id"             BIGINT  NOT NULL,
  "version"        INTEGER,
  "active"         BOOLEAN NOT NULL DEFAULT TRUE,
  "sell_from_date" DATE    NOT NULL,
  "sell_to_date"   DATE    NOT NULL,
  "user_id"        BIGINT  NOT NULL,
  "currency_id"    BIGINT  NOT NULL,
  "description"    TEXT,

  "modified_by"    BIGINT  NOT NULL DEFAULT 0,
  "modified"       TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "pricelist_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "pricelist_to_user_fkey" FOREIGN KEY ("user_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE,
  CONSTRAINT "pricelist_to_currency_fkey" FOREIGN KEY ("currency_id") REFERENCES "currency" ("id")

);
ALTER TABLE "pricelist" OWNER TO pricegsmowner;

CREATE SEQUENCE "pricelist_seq";
ALTER TABLE "pricelist_seq" OWNER TO pricegsmowner;

CREATE TABLE "pricelist_position" (
  "id"                 BIGINT         NOT NULL,
  "version"            INTEGER,
  "active"             BOOLEAN        NOT NULL DEFAULT TRUE,
  "pricelist_id"       BIGINT         NOT NULL,
  "price"              NUMERIC(10, 2) NOT NULL,
  "product_id"         BIGINT         NOT NULL,
  "specification_id"   BIGINT,
  "amount"             INTEGER        NOT NULL DEFAULT 1,
  "min_order_quantity" INTEGER        NOT NULL DEFAULT 1,
  "description"        TEXT,

  "modified_by"        BIGINT         NOT NULL DEFAULT 0,
  "modified"           TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "pricelist_position_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "pricelist_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  CONSTRAINT "pricelist_to_specification_fkey" FOREIGN KEY ("specification_id") REFERENCES "specification" ("id"),
  CONSTRAINT "pricelist_to_pricelist_fkey" FOREIGN KEY ("pricelist_id") REFERENCES "pricelist" ("id") ON DELETE CASCADE

);
ALTER TABLE "pricelist_position" OWNER TO pricegsmowner;

CREATE SEQUENCE "pricelist_position_seq";
ALTER TABLE "pricelist_position_seq" OWNER TO pricegsmowner;

CREATE TABLE "exchange" (
  "id"               BIGINT                   NOT NULL,
  "exchange_date"    TIMESTAMP WITH TIME ZONE NOT NULL,
  "value"            NUMERIC(10, 3)           NOT NULL,
  "from_currency_id" BIGINT                   NOT NULL,
  "to_currency_id"   BIGINT                   NOT NULL,

  "modified_by"      BIGINT                   NOT NULL DEFAULT 0,
  "modified"         TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "exchange_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "exchange_to_from_currency_fkey" FOREIGN KEY ("from_currency_id") REFERENCES "currency" ("id") ON DELETE CASCADE,
  CONSTRAINT "exchange_to_to_currency_id_fkey" FOREIGN KEY ("to_currency_id") REFERENCES "currency" ("id") ON DELETE CASCADE
);
ALTER TABLE "exchange" OWNER TO pricegsmowner;

CREATE SEQUENCE "exchange_seq";
ALTER TABLE "exchange_seq" OWNER TO pricegsmowner;

CREATE TABLE "partner" (
  "id"             BIGINT  NOT NULL,
  "user_id"        BIGINT  NOT NULL,
  "partner_id"     BIGINT  NOT NULL,
  "approved"       BOOLEAN NOT NULL DEFAULT FALSE,
  "confirmed"      BOOLEAN NOT NULL DEFAULT FALSE,
  "show_pricelist" BOOLEAN NOT NULL DEFAULT TRUE,

  "modified_by"    BIGINT  NOT NULL DEFAULT 0,
  "modified"       TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "partner_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "partner_to_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE,
  CONSTRAINT "partner_to_partner_id_fkey" FOREIGN KEY ("partner_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE
);
ALTER TABLE "partner" OWNER TO pricegsmowner;

CREATE SEQUENCE "partner_seq";
ALTER TABLE "partner_seq" OWNER TO pricegsmowner;

CREATE TABLE "delivery_place" (
  "id"          BIGINT       NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "region_id"   BIGINT       NOT NULL,
  "description" TEXT,

  "modified_by" BIGINT       NOT NULL DEFAULT 0,
  "modified"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "delivery_place_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "delivery_place_to_region_fkey" FOREIGN KEY ("region_id") REFERENCES "region" ("id"),
  CONSTRAINT "delivery_place_name_unique" UNIQUE (name)

);
ALTER TABLE "delivery_place" OWNER TO pricegsmowner;

CREATE SEQUENCE "delivery_place_seq" START 5;
ALTER TABLE "delivery_place_seq" OWNER TO pricegsmowner;

INSERT INTO delivery_place (id, name, region_id) VALUES (1, 'Савелово', 1);
INSERT INTO delivery_place (id, name, region_id) VALUES (2, 'Митино', 1);
INSERT INTO delivery_place (id, name, region_id) VALUES (3, 'Горбушка', 1);
INSERT INTO delivery_place (id, name, region_id) VALUES (4, 'Москва', 1);

CREATE TABLE "persistent_logins" (
  "series"    VARCHAR(64) NOT NULL,
  "username"  VARCHAR(64) NOT NULL,
  "token"     VARCHAR(64) NOT NULL,
  "last_used" TIMESTAMP   NOT NULL,
  CONSTRAINT "persistent_logins_pkey" PRIMARY KEY ("series")
);
ALTER TABLE "persistent_logins" OWNER TO pricegsmowner;
