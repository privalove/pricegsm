-- add specification column to order position
ALTER TABLE "order_position" add column "specification_id" BIGINT;

ALTER TABLE "order_position" add CONSTRAINT "order_position_to_specification_fkey" FOREIGN KEY ("specification_id") REFERENCES "specification" ("id");

UPDATE "order_position" SET specification_id = 1;
