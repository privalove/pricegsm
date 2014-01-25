update vendor set name = 'Apple' where id = 1;
update vendor set name = 'Sony' where id = 3;
update vendor set name = 'Samsung' where id = 5;

update vendor set active = false where id in (2,4,6);

update product set vendor_id = 1 where vendor_id = 2;
update product set vendor_id = 3 where vendor_id = 4;
update product set vendor_id = 5 where vendor_id = 6;