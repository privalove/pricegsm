package com.pricegsm.service;

import com.pricegsm.dao.OrderDao;
import com.pricegsm.domain.Order;
import com.pricegsm.domain.OrderPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderService
        extends GlobalEntityService<Order> {

    @Autowired
    private OrderDao dao;

    @Override
    protected OrderDao getDao() {
        return dao;
    }

    @Override
    protected Order preSave(Order entity) {
        Set<OrderPosition> orderPositions = entity.getOrderPositions();
        for (OrderPosition orderPosition : orderPositions) {
            orderPosition.setOrder(entity);
        }
        return entity;
    }

    public List<Order> findByBuyer(long buyerId) {
        return getDao().findByBuyer(buyerId);
    }

    public Order getCurrentUserOrderById(long buyerId, long orderId) {
        return getDao().findByUserIdOrderId(buyerId, orderId);
    }
}
