package com.pricegsm.dao;

import com.pricegsm.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao
        extends GlobalEntityDao<Product> {

    public List<Product> findByYandexId(String yandexId) {
        return getEntityManager()
                .createQuery("select p from Product p where p.yandexId = :yandexId")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }
}
