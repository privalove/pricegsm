-- add table marketplace_filter
CREATE TABLE "marketplace_filter" (
  "id"            BIGINT         NOT NULL,
  "user_id"       BIGINT         NOT NULL,
  "vendor_id"     BIGINT         NOT NULL,
  "product_id"    BIGINT         NOT NULL,

  "modified_by"   BIGINT  NOT NULL DEFAULT 0,
  "modified"      TIMESTAMP WITH TIME ZONE,
  CONSTRAINT "marketplace_filter_pk" PRIMARY KEY ("id"),
  CONSTRAINT "user_fkey" FOREIGN KEY ("user_id") REFERENCES "pricegsm_user" ("id") ON DELETE CASCADE,
  CONSTRAINT "vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("id") ON DELETE CASCADE,
  CONSTRAINT "product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE
);

ALTER TABLE "marketplace_filter" OWNER TO pricegsmowner;

CREATE SEQUENCE "marketplace_filter_seq";
ALTER TABLE "marketplace_filter_seq" OWNER TO pricegsmowner;
