alter table pricegsm_user alter column seller_delivery_from set default '10:00';
alter table pricegsm_user alter column seller_delivery_to set default '17:00';
alter table pricegsm_user alter column seller_pickup_from set default '10:00';
alter table pricegsm_user alter column seller_pickup_to set default '17:00';
alter table pricegsm_user alter column buyer_delivery_from set default '10:00';
alter table pricegsm_user alter column buyer_delivery_to set default '17:00';
alter table pricegsm_user alter column seller_delivery set default true;
alter table pricegsm_user alter column seller_delivery_paid set default true;
alter table pricegsm_user alter column seller_delivery_cost set default 300;

update pricegsm_user set seller_delivery_from = '10:00',
  seller_delivery_to = '17:00',
  seller_pickup_from = '10:00',
  seller_pickup_to = '17:00',
  buyer_delivery_from = '10:00',
  buyer_delivery_to = '17:00',
  seller_delivery = true,
  seller_delivery_paid = true,
  seller_delivery_cost = 300;