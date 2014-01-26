Alter table base_user drop constraint base_user_email_unique;
CREATE UNIQUE INDEX base_user_email_unique ON base_user (lower(email));