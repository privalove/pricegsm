CREATE TABLE "currency" (
  "id"          INTEGER,
  "name"        VARCHAR(255) NOT NULL,
  "code"        VARCHAR(255),
  "symbol"      VARCHAR(255),

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "currency_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "currency" OWNER TO pricegsmowner;

CREATE SEQUENCE "currency_seq";
ALTER TABLE "currency_seq" OWNER TO pricegsmowner;


CREATE TABLE "region" (
  "id"          INTEGER      NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "description" TEXT,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "region_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "region" OWNER TO pricegsmowner;

CREATE SEQUENCE "region_seq";
ALTER TABLE "region_seq" OWNER TO pricegsmowner;


CREATE TABLE "base_user" (
  "id"          INTEGER      NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "email"       VARCHAR(255) NOT NULL,
  "password"    VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "base_user_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "base_user" OWNER TO pricegsmowner;

CREATE SEQUENCE "base_user_seq";
ALTER TABLE "base_user_seq" OWNER TO pricegsmowner;

CREATE TABLE "pricegsm_user" (
  "id"                    INTEGER        NOT NULL,
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
  "seller_delivery_cost"  NUMERIC(10, 2),
  "buyer_delivery_place"  VARCHAR(255),
  "buyer_delivery_from"   TIME,
  "buyer_delivery_to"     TIME,
  "region_id"             INTEGER,
  "balance"               NUMERIC(10, 2) NOT NULL DEFAULT 0,
  CONSTRAINT "user_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "user_to_base_user_fkey" FOREIGN KEY ("id") REFERENCES "base_user" ("id"),
  CONSTRAINT "user_to_region_fkey" FOREIGN KEY ("region_id") REFERENCES "region" ("id")
);
ALTER TABLE "pricegsm_user" OWNER TO pricegsmowner;

CREATE TABLE administrator (
  "id" INTEGER NOT NULL,
  CONSTRAINT "administrator_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "administrator_to_base_user_fkey" FOREIGN KEY ("id") REFERENCES "base_user" ("id")
);
ALTER TABLE "administrator" OWNER TO pricegsmowner;

CREATE TABLE "color" (
  "id"           INTEGER      NOT NULL,
  "name"         VARCHAR(255) NOT NULL,
  "yandex_color" VARCHAR(255),
  "active"       BOOLEAN      NOT NULL DEFAULT TRUE,
  "description"  TEXT,

  "modified"     INTEGER,
  "modified_by"  TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "color_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "color" OWNER TO pricegsmowner;

CREATE SEQUENCE "color_seq";
ALTER TABLE "color_seq" OWNER TO pricegsmowner;

CREATE TABLE "vendor" (
  "id"          INTEGER      NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "description" TEXT,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "vendor_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "vendor" OWNER TO pricegsmowner;

CREATE SEQUENCE "vendor_seq";
ALTER TABLE "vendor_seq" OWNER TO pricegsmowner;

CREATE TABLE "product" (
  "id"          INTEGER      NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "yandex_id"   INTEGER      NOT NULL,
  "vendor_id"   INTEGER      NOT NULL,
  "color_id"    INTEGER      NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "description" TEXT,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "product_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "product_to_vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("id") ON DELETE CASCADE,
  CONSTRAINT "product_to_color_fkey" FOREIGN KEY ("color_id") REFERENCES "color" ("id") ON DELETE CASCADE
);
ALTER TABLE "product" OWNER TO pricegsmowner;

CREATE SEQUENCE "product_seq";
ALTER TABLE "product_seq" OWNER TO pricegsmowner;


CREATE TABLE "order" (
  "id"            INTEGER        NOT NULL,
  "buyer_id"      INTEGER        NOT NULL,
  "seller_id"     INTEGER        NOT NULL,
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
  "delivery_date" TIMESTAMP WITH TIME ZONE,
  "description"   TEXT,
  "total_price"   NUMERIC(10, 2) NOT NULL,
  "total_amount"  INTEGER        NOT NULL,

  "modified"      INTEGER,
  "modified_by"   TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "order_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "order_to_seller_fkey" FOREIGN KEY ("seller_id") REFERENCES "pricegsm_user" ("id"),
  CONSTRAINT "order_to_buyer_fkey" FOREIGN KEY ("buyer_id") REFERENCES "pricegsm_user" ("id")
);
ALTER TABLE "order" OWNER TO pricegsmowner;

CREATE SEQUENCE "order_seq";
ALTER TABLE "order_seq" OWNER TO pricegsmowner;

CREATE TABLE "order_position" (
  "id"          INTEGER        NOT NULL,
  "order_id"    INTEGER        NOT NULL,
  "position"    INTEGER,
  "product_id"  INTEGER        NOT NULL,
  "total_price" NUMERIC(10, 2) NOT NULL,
  "price"       NUMERIC(10, 2) NOT NULL,
  "amount"      INTEGER        NOT NULL,
  "version"     INTEGER,
  "currency_id" INTEGER        NOT NULL,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "order_position_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "order_position_to_order_fkey" FOREIGN KEY ("order_id") REFERENCES "order" ("id") ON DELETE CASCADE,
  CONSTRAINT "order_position_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  CONSTRAINT "order_position_to_currency_fkey" FOREIGN KEY ("currency_id") REFERENCES "currency" ("id")
);
ALTER TABLE "order_position" OWNER TO pricegsmowner;

CREATE SEQUENCE "order_position_seq";
ALTER TABLE "order_position_seq" OWNER TO pricegsmowner;

CREATE TABLE "yandex_price" (
  "id"          INTEGER                  NOT NULL,
  "position"    INTEGER                  NOT NULL,
  "sell_date"   TIMESTAMP WITH TIME ZONE NOT NULL,
  "product_id"  INTEGER                  NOT NULL,
  "price_rub"   NUMERIC(10, 2)           NOT NULL,
  "price_eur"   NUMERIC(10, 2),
  "price_usd"   NUMERIC(10, 2),
  "shop"        VARCHAR(255),
  "link"        VARCHAR(255),

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "yandex_price_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "yandex_price_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE
);
ALTER TABLE "yandex_price" OWNER TO pricegsmowner;

CREATE SEQUENCE "yandex_price_seq";
ALTER TABLE "yandex_price_seq" OWNER TO pricegsmowner;

CREATE TABLE "world_price" (
  "id"          INTEGER                  NOT NULL,
  "position"    INTEGER                  NOT NULL,
  "sell_date"   TIMESTAMP WITH TIME ZONE NOT NULL,
  "product_id"  INTEGER                  NOT NULL,
  "price_rub"   NUMERIC(10, 2),
  "price_eur"   NUMERIC(10, 2),
  "price_usd"   NUMERIC(10, 2),

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "world_price_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "world_price_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE
);
ALTER TABLE "world_price" OWNER TO pricegsmowner;

CREATE SEQUENCE "world_price_seq";
ALTER TABLE "world_price_seq" OWNER TO pricegsmowner;

CREATE TABLE "pricelist" (
  "id"                 INTEGER                  NOT NULL,
  "version"            INTEGER,
  "active"             BOOLEAN                  NOT NULL DEFAULT TRUE,
  "sell_from_date"     TIMESTAMP WITH TIME ZONE NOT NULL,
  "sell_to_date"       TIMESTAMP WITH TIME ZONE NOT NULL,
  "price"              NUMERIC(10, 2)           NOT NULL,
  "product_id"         INTEGER                  NOT NULL,
  "user_id"            INTEGER                  NOT NULL,
  "currency_id"        INTEGER                  NOT NULL,
  "amount"             INTEGER                  NOT NULL DEFAULT 1,
  "min_order_quantity" INTEGER                  NOT NULL DEFAULT 1,
  "description"        TEXT,

  "modified"           INTEGER,
  "modified_by"        TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "pricelist_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "pricelist_to_user_fkey" FOREIGN KEY ("user_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE,
  CONSTRAINT "pricelist_to_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id"),
  CONSTRAINT "pricelist_to_currency_fkey" FOREIGN KEY ("currency_id") REFERENCES "currency" ("id")

);
ALTER TABLE "pricelist" OWNER TO pricegsmowner;

CREATE SEQUENCE "pricelist_seq";
ALTER TABLE "pricelist_seq" OWNER TO pricegsmowner;

CREATE TABLE "exchange" (
  "id"               INTEGER                  NOT NULL,
  "exchange_date"    TIMESTAMP WITH TIME ZONE NOT NULL,
  "value"            NUMERIC(10, 3)           NOT NULL,
  "from_currency_id" INTEGER                  NOT NULL,
  "to_currency_id"   INTEGER                  NOT NULL,

  "modified"         INTEGER,
  "modified_by"      TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "exchange_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "exchange_to_from_currency_fkey" FOREIGN KEY ("from_currency_id") REFERENCES "currency" ("id") ON DELETE CASCADE,
  CONSTRAINT "exchange_to_to_currency_id_fkey" FOREIGN KEY ("to_currency_id") REFERENCES "currency" ("id") ON DELETE CASCADE
);
ALTER TABLE "exchange" OWNER TO pricegsmowner;

CREATE SEQUENCE "exchange_seq";
ALTER TABLE "exchange_seq" OWNER TO pricegsmowner;

CREATE TABLE "partner" (
  "id"             INTEGER NOT NULL,
  "user_id"        INTEGER NOT NULL,
  "partner_id"     INTEGER NOT NULL,
  "approved"       BOOLEAN NOT NULL DEFAULT FALSE,
  "confirmed"      BOOLEAN NOT NULL DEFAULT FALSE,
  "show_pricelist" BOOLEAN NOT NULL DEFAULT TRUE,

  "modified"       INTEGER,
  "modified_by"    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "partner_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "partner_to_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE,
  CONSTRAINT "partner_to_partner_id_fkey" FOREIGN KEY ("partner_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE
);
ALTER TABLE "partner" OWNER TO pricegsmowner;

CREATE SEQUENCE "partner_seq";
ALTER TABLE "partner_seq" OWNER TO pricegsmowner;

CREATE TABLE "delivery_place" (
  "id"          INTEGER      NOT NULL,
  "name"        VARCHAR(255) NOT NULL,
  "active"      BOOLEAN      NOT NULL DEFAULT TRUE,
  "region_id"   INTEGER      NOT NULL,
  "description" TEXT,

  "modified"    INTEGER,
  "modified_by" TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "delivery_place_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "delivery_place_to_region_fkey" FOREIGN KEY ("region_id") REFERENCES "region" ("id")
);
ALTER TABLE "delivery_place" OWNER TO pricegsmowner;

CREATE SEQUENCE "delivery_place_seq";
ALTER TABLE "delivery_place_seq" OWNER TO pricegsmowner;

