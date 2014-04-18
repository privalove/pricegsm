package com.pricegsm.dao;

import com.pricegsm.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao
        extends GlobalEntityDao<Order> {
    public List<Order> findByBuyer(long buyerId) {
        List buyer = getEntityManager().createQuery("select o from Order o where o.buyer.id = :buyer")
                .setParameter("buyer", buyerId)
                .getResultList();
        return buyer;
    }
}
