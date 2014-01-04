package com.pricegsm.hibernate;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.spi.PersistenceProvider;

/**
 * Support new hibernate 4.0
 * TODO update hibernate later to avoid false warning https://hibernate.atlassian.net/browse/HHH-8735
 */
public class Hibernate4JpaVendorAdapter
        extends HibernateJpaVendorAdapter {

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return new HibernatePersistenceProvider();
    }
}
