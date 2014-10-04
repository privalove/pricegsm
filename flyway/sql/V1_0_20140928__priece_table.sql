-- add table
CREATE TABLE "prices" (
  "id"                           BIGINT         NOT NULL,
  "price_list_position_id"       BIGINT         NOT NULL,
  "price"                        NUMERIC(10, 2) NOT NULL,
  "min_order_quantity"                          INTEGER        NOT NULL,

  "modified_by"                  BIGINT  NOT NULL DEFAULT 0,
  "modified"                     TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "prices_pk" PRIMARY KEY ("id"),
  CONSTRAINT "price_list_position_fkey" FOREIGN KEY ("price_list_position_id") REFERENCES "pricelist_position" ("id") ON DELETE CASCADE
);

ALTER TABLE "prices" OWNER TO pricegsmowner;

CREATE SEQUENCE "prices_seq";
ALTER TABLE "prices_seq" OWNER TO pricegsmowner;

-- fill table by test data
UPDATE "pricelist_position" SET price = 100 WHERE price < 100;

INSERT INTO "prices" (id, price_list_position_id, price, min_order_quantity)
   select (nextval('prices_seq')) ,id, price, min_order_quantity from "pricelist_position"
   RETURNING id;

INSERT INTO "prices" (id, price_list_position_id, price, min_order_quantity)
   select (nextval('prices_seq')), id, (price-10), (min_order_quantity + 10) from "pricelist_position";

INSERT INTO "prices" (id, price_list_position_id, price, min_order_quantity)
   select (nextval('prices_seq')), id, (price-30), (min_order_quantity + 100) from "pricelist_position";

