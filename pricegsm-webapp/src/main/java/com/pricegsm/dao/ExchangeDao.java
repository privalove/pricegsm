package com.pricegsm.dao;

import com.pricegsm.domain.Exchange;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class ExchangeDao
        extends GlobalEntityDao<Exchange> {

    public Exchange getLast(long from, long to) {
        try {
            return (Exchange)getEntityManager()
                    .createQuery("select e from Exchange e where e.from.id = :from and e.to.id = :to order by e.date desc")
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
