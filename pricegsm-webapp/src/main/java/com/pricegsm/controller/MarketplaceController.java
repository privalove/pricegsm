package com.pricegsm.controller;

import com.pricegsm.domain.BaseUser;
import com.pricegsm.domain.Order;
import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.*;
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

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/marketplace", method = RequestMethod.GET)
    public void marketplace() {
    }

    @RequestMapping(value = "/marketplace/pricelists.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult pricelists() {
        return OperationResult.ok().payload("pricelists", priceListService.findAll());
    }

    @RequestMapping(value = "/marketplace/filtersData.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult vendors() {
        return OperationResult.ok()
                .payload("vendors", vendorService.findAll())
                .payload("products", productService.findAll())
                .payload("sellers", userService.findAll());
    }

    @RequestMapping(value = "/marketplace/buyer.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult buyer() {
        BaseUser currentUser = principalHolder.getCurrentUser();
        return OperationResult.ok().payload("buyer", currentUser);
    }

    @RequestMapping(value = "/marketplace/order.json", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult order(@RequestBody Order order) {

        //todo add check for error
        orderService.save(order);

        return OperationResult.ok().payload("order", order);
    }
}
