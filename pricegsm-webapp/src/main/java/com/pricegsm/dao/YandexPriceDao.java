package com.pricegsm.dao;

import com.pricegsm.domain.Product;
import com.pricegsm.domain.YandexPrice;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Repository
public class YandexPriceDao
        extends GlobalEntityDao<YandexPrice> {

    /**
     * @return List of pairs {Product Yandex ID, Date}
     */
    public List<Object[]> findLast() {
        return getEntityManager()
                .createQuery("select p.yandexId, max(y.date) from YandexPrice as y right outer join y.product as p "
                        + " where p.active = true group by p.yandexId order by max(y.date)")
                .getResultList();
    }

    public Date findLastDate(long productId) {
        return (Date) getEntityManager()
                .createQuery("select max(y.date) from YandexPrice y where y.product.id = :productId")
                .setParameter("productId", productId)
                .getSingleResult();
    }

    public YandexPrice findLastMinPrice(long productId) {
        try {
            return (YandexPrice) getEntityManager()
                    .createQuery("select y from YandexPrice y where y.product.id = :productId order by y.date desc, y.priceRub asc")
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public YandexPrice findByDateMinPrice(long productId, Date date) {
        try {
            return (YandexPrice) getEntityManager()
                    .createQuery("select y from YandexPrice y where y.product.id = :productId and y.date <= :date order by y.date desc, y.priceRub asc")
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<YandexPrice> findByDateForProducts(Date date, List<Product> products) {
        return getEntityManager()
                .createQuery("select y from YandexPrice y where y.product in :products and y.date = (select max(date) from YandexPrice where date <= :date) order by y.position, y.shop")
                .setParameter("date", date)
                .setParameter("products", products)
                .getResultList();
    }
}
