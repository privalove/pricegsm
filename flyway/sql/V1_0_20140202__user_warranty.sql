alter table pricegsm_user add column seller_free_replacement integer not null default 0;
alter table pricegsm_user add column seller_free_repair integer not null default 0;
alter table pricegsm_user add column seller_warranty_additional varchar(255);
alter table pricegsm_user add column seller_pickup_place_addition varchar(255);
