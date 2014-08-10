package com.pricegsm.service;

import com.pricegsm.dao.OrderDao;
import com.pricegsm.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService
        extends GlobalEntityService<Order> {

    @Autowired
    private OrderDao dao;

    @Override
    protected OrderDao getDao() {
        return dao;
    }

    public List<Order> findByBuyer(long buyerId) {
        return getDao().findByBuyer(buyerId);
    }

    public Order getCurrentUserOrderById(long buyerId, long orderId) {
        return getDao().findByUserIdOrderId(buyerId, orderId);
    }
}
