package com.pricegsm.dao;

import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Repository
public class ProductDao
        extends GlobalEntityDao<Product> {

    protected Iterable<String> getOrderByProperties() {
        return Arrays.asList("vendor.id", "type.id", "name", "color.id");
    }

    public List<Product> findByYandexId(String yandexId) {
        return getEntityManager()
                .createQuery("select p from Product p where p.active = true and p.yandexId = :yandexId")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }

    public List<Product> findByVendor(long vendor) {
        return getEntityManager()
                .createQuery("select p from Product p where p.vendor.id = :vendor")
                .setParameter("vendor", vendor)
                .getResultList();
    }

    public List<Product> findActiveByVendorOrderByVendorAndName(long vendor, List<Long> types) {
        return getEntityManager()
                .createQuery("select p from Product p "
                        + " inner join p.color c "
                        + " inner join p.type t "
                        + " where p.active = true "
                        + " and p.vendor.id = :vendor "
                        + " and p.type.id in (:types) "
                        + " order by  t.name, p.name, c.name")
                .setParameter("vendor", vendor)
                .setParameter("types", types)
                .getResultList();
    }

    public List<Color> findColors(String yandexId) {
        return getEntityManager()
                .createQuery("select distinct c from Product p inner join p.color c where p.yandexId = :yandexId order by c.name")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }

    @Transactional
    public void deleteProductsByYandexId(String yandexId) {
        getEntityManager()
                .createQuery("delete from Product p where p.yandexId = :yandexId")
                .setParameter("yandexId", yandexId)
                .executeUpdate();
    }

    public List<Product> findAllByYandexId(String yandexId) {
        return getEntityManager()
                .createQuery("select p from Product p where p.yandexId = :yandexId")
                .setParameter("yandexId", yandexId)
                .getResultList();
    }
}
