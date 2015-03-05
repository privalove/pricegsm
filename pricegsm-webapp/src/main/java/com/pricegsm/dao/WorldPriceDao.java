package com.pricegsm.dao;

import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

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

    public List<WorldPrice> findByDateForProduct(long productId) {
        return getEntityManager()
                .createQuery("select w from WorldPrice w "
                        + " where w.product.id = :product "
                        + " and w.date = ("
                        + "     select max(date) from WorldPrice "
                        + "     where product.id = :product) "
                        + " order by w.priceRub")
                .setParameter("product", productId)
                .getResultList();
    }
}
