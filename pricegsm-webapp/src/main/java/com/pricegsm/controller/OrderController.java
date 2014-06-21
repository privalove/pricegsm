package com.pricegsm.controller;

import com.pricegsm.domain.*;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.OrderService;
import com.pricegsm.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Заказы
 */
@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
     PrincipalHolder principalHolder;

    @Autowired
    private PriceListService priceListService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public void order() {
    }

    @RequestMapping(value = "/order/orders.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult orders() {
        BaseUser currentUser = principalHolder.getCurrentUser();
        return OperationResult.ok().payload("orders", orderService.findByBuyer(currentUser.getId()));
    }

    @RequestMapping(value = "/order/{sellerId}/{position}/pricelist.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult priceList(@PathVariable int sellerId, @PathVariable int position) {
        return OperationResult.ok().payload("orderPositionTemplate", new OrderPosition())
                .payload("priceList", priceListService.getPriceList(sellerId, position));
    }
}
