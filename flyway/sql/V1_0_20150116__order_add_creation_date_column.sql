-- make organization name unique
alter table "order" add column creation_date TIMESTAMP WITH TIME ZONE;

UPDATE "order" SET creation_date = send_date;
