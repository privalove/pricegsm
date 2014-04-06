package com.pricegsm.controller;

import com.pricegsm.domain.Order;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.OrderService;
import com.pricegsm.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Торговая площадка
 *
 * @author andreybugaev
 * @since 25.12.2013
 */
@Controller
public class MarketplaceController {

    @Autowired
    private PriceListService priceListService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrincipalHolder principalHolder;

    @RequestMapping(value = "/marketplace", method = RequestMethod.GET)
    public void marketplace() {
    }

    @RequestMapping(value = "/marketplace/pricelists.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult pricelists() {
        return OperationResult.ok().payload("pricelists", priceListService.findAll());
    }

    @RequestMapping(value = "/marketplace/orders.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult orders() {
        return OperationResult.ok().payload("orders", orderService.findByBuyer(principalHolder.getCurrentUser().getId()));
    }

    @RequestMapping(value = "/marketplace/order.json", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult order(@RequestBody Order order) {

        orderService.save(order);

        return OperationResult.ok().payload("order", order);
    }

}
