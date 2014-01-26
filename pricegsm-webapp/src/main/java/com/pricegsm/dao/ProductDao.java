package com.pricegsm.dao;

import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao
        extends GlobalEntityDao<Product> {

    public List<Product> findByYandexId(String yandexId) {
        return getEntityManager()
                .createQuery("select p from Product p where p.active = true and p.yandexId = :yandexId")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }

    public List<Product> findActiveByVendorOrderByVendorAndName(long vendor) {
        return getEntityManager()
                .createQuery("select p from Product p inner join p.vendor v inner join p.color c inner join p.type t where p.active = true and p.vendor.id = :vendor order by  t.name, v.name, p.name, c.name")
                .setParameter("vendor", vendor)
                .getResultList();
    }

    public List<Color> findColors(String yandexId) {
        return getEntityManager()
                .createQuery("select distinct c from Product p inner join p.color c where p.yandexId = :yandexId order by c.name")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }
}
