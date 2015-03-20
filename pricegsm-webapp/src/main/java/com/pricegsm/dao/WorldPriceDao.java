package com.pricegsm.dao;

import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;
import com.pricegsm.parser.PriceListDescriptor;
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
                    .createQuery("select y from WorldPrice y where y.product.id = :productId order by y.date desc, y.priceUsd asc")
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
                    .createQuery("select y from WorldPrice y where y.product.id = :productId and y.date <= :date order by y.date desc, y.priceUsd asc")
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<WorldPrice> findByDateForProduct(long productId, PriceListDescriptor descriptor) {
        //noinspection unchecked
        return getEntityManager()
                .createQuery("select w from WorldPrice w "
                        + " where w.product.id = :product and w.seller = :seller "
                        + " and w.date = ("
                        + "     select max(date) from WorldPrice "
                        + "     where product.id = :product and seller = :seller) "
                        + " order by w.priceRub")
                .setParameter("product", productId)
                .setParameter("seller", descriptor.getName())
                .getResultList();
    }
}
