alter table "pricegsm_user" add column deadline time without time zone DEFAULT '17:00:00' NOT NULL;
alter table "pricegsm_user" add column organization_name character varying(255);

alter table "pricelist" add column phone character varying(255);