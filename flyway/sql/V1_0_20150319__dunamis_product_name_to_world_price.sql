-- add description to world_price
alter table "product" add column "dunamis_product_name" varchar(255);

alter table "world_price" add column "seller" varchar(255);

UPDATE "world_price" SET seller = 'masterfone';