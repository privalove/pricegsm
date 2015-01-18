package com.pricegsm.dao;

import com.pricegsm.domain.Order;
import com.pricegsm.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    public Order findByUserIdOrderId(long buyerId, long orderId) {
        return (Order) getEntityManager().createQuery(
                "select o from Order o where o.id = :id and o.buyer.id = :buyer")
                .setParameter("id", orderId)
                .setParameter("buyer", buyerId)
                .getSingleResult();
    }

    public Date getUserLastOrder(long buyerId, long sellerId) {
        return (Date) getEntityManager().createQuery(
                "select max(o.sendDate) from Order o " +
                        " where o.buyer.id = :buyerId " +
                        " and o.seller.id = :sellerId")
                .setParameter("buyerId", buyerId)
                .setParameter("sellerId", sellerId)
                .getSingleResult();
    }
}
