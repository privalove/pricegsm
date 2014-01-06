package com.pricegsm.dao;

import com.pricegsm.domain.YandexPrice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class YandexPriceDao
        extends GlobalEntityDao<YandexPrice> {

    /**
     * @return List of pairs {Product Yandex ID, Date}
     */
    public List<Object[]> findLast() {
        return getEntityManager()
                .createQuery("select p.yandexId, max(y.date) from YandexPrice as y right outer join y.product as p where p.active = true group by p.yandexId order by max(y.date)")
                .getResultList();
    }
}
