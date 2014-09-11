alter table "pricegsm_user" add column deadline time without time zone DEFAULT '17:00:00' NOT NULL;

alter table "pricelist" add column phone character varying(255);