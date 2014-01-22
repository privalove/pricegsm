alter table product add column search_query varchar(255);
alter table product alter column yandex_id drop NOT NULL;

update product set search_query = 'iphone 5s 16gb' where id in (1,2,3);
update product set search_query = 'iphone 5s 32gb' where id in (4,5,6);
update product set search_query = 'iphone 5s 64gb' where id in (7,8,9);
update product set search_query = 'iphone 5c 16gb' where id in (10,11,12,13,14);
update product set search_query = 'iphone 5c 32gb' where id in (15,16,17,18,19);
