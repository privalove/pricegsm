package com.pricegsm.controller;

import com.pricegsm.domain.BaseUser;
import com.pricegsm.domain.Order;
import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Заказы
 */
@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    private PrincipalHolder principalHolder;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public void order() {
        long id = principalHolder.getCurrentUser().getId();
        principalHolder.getCurrentUser().getId();
    }

    @RequestMapping(value = "/order/orders.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult orders() {
        List<Order> byBuyer = orderService.findByBuyer(principalHolder.getCurrentUser().getId());
        return OperationResult.ok()
                .payload("orders", byBuyer);
    }
}
