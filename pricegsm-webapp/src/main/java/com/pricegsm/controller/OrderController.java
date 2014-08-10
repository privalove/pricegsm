package com.pricegsm.controller;

import com.pricegsm.domain.*;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.OrderService;
import com.pricegsm.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @RequestMapping(value = "/order/orders.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult orders() {
        BaseUser currentUser = principalHolder.getCurrentUser();
        return OperationResult.ok().payload("orders", orderService.findByBuyer(currentUser.getId()));
    }

    @RequestMapping(value = "/order/{sellerId}/{position}/pricelist.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult priceList(@PathVariable int sellerId, @PathVariable int position) {
        return OperationResult.ok().payload("orderPositionTemplate", new OrderPosition())
                .payload("priceList", priceListService.getPriceList(sellerId, position));
    }

    @RequestMapping(value = "/order/{orderId}/order.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult saveOrder(@PathVariable int orderId, @RequestBody Order order, BindingResult result) {

        if (result.hasErrors()) {
            BaseUser currentUser = principalHolder.getCurrentUser();
            return OperationResult
                    .validation()
                    .payload("order", orderService.getCurrentUserOrderById(orderId, currentUser.getId()));
        }

        orderService.save(order);

        return OperationResult.ok().payload("order", orderService.load(order.getId()));
    }

    @RequestMapping(value = "/order/{orderId}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteOrder(@PathVariable Long orderId) {
        try {
            switch (orderService.delete(orderId)) {
                case OK:
                    break;
            }
        } catch (Exception e) {
        }
        return;
    }

}
