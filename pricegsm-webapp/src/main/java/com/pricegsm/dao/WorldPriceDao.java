package com.pricegsm.dao;

import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;

@Repository
public class WorldPriceDao
        extends GlobalEntityDao<WorldPrice> {
    public WorldPrice findLast(long productId) {
        try {
            return (WorldPrice) getEntityManager()
                    .createQuery("select y from WorldPrice y where y.product.id = :productId order by y.date desc")
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public WorldPrice findByDate(long productId, Date date) {
        try {
            return (WorldPrice) getEntityManager()
                    .createQuery("select y from WorldPrice y where y.product.id = :productId and y.date <= :date order by y.date desc")
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
