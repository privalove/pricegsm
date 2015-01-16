-- make organization name unique
ALTER TABLE "pricegsm_user" ADD CONSTRAINT organization_name_unique UNIQUE (organization_name);
