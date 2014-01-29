ALTER TABLE pricelist_position DROP CONSTRAINT pricelist_to_pricelist_fkey;
ALTER TABLE pricelist_position DROP COLUMN pricelist_id;
ALTER TABLE pricelist_position ADD COLUMN pricelist_user_id BIGINT NOT NULL;
ALTER TABLE pricelist_position ADD COLUMN pricelist_position INTEGER NOT NULL;

ALTER TABLE pricelist DROP CONSTRAINT pricelist_pkey;
ALTER TABLE pricelist DROP COLUMN id;
ALTER TABLE pricelist ADD COLUMN position INTEGER NOT NULL;
ALTER TABLE pricelist ADD CONSTRAINT pricelist_pkey PRIMARY KEY (user_id, position);

ALTER TABLE pricelist_position ADD CONSTRAINT pricelist_to_pricelist_fkey FOREIGN KEY (pricelist_user_id, pricelist_position)
REFERENCES pricelist (user_id, position) ON DELETE CASCADE;
