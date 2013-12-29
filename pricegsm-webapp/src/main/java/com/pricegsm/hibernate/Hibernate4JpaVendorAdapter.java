package com.pricegsm.hibernate;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.spi.PersistenceProvider;

/**
 * Support new hibernate 4.0
 */
public class Hibernate4JpaVendorAdapter
        extends HibernateJpaVendorAdapter {

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return new HibernatePersistenceProvider();
    }
}
