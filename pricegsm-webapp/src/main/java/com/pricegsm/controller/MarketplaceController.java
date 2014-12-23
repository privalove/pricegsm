package com.pricegsm.controller;

import com.pricegsm.domain.*;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Set;

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
        return OperationResult.ok().payload("buyer", userService.load(currentUser.getId()));
    }

    @RequestMapping(value = "/marketplace/order.json", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult order(@RequestBody Order order, BindingResult result) {

        if (result.hasErrors()) {
            return OperationResult.validation();
        }

        orderService.save(order);

        return OperationResult.ok().payload("order", order);
    }

    @RequestMapping(value = "/marketplace/marketplaceFilter.json", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult saveFilter(
            @RequestBody MarketplaceFilter filter,
            BindingResult result) {

        if (result.hasErrors()) {
            return OperationResult.validation();
        }

        User currentUser = (User) principalHolder.getCurrentUser();
        User user = userService.load(currentUser.getId());

        filter.setUser(user);

        Set<MarketplaceFilter> filters = user.getMarketplaceFilters();
        if (filters.size() == 15) {

            MarketplaceFilter oldestMarketplaceFilter = new MarketplaceFilter();
            oldestMarketplaceFilter.setModified(new Date());

            for (MarketplaceFilter marketplaceFilter : filters) {
                if (marketplaceFilter.getModified().before(oldestMarketplaceFilter.getModified())) {
                    oldestMarketplaceFilter = marketplaceFilter;
                }
            }

            filters.remove(oldestMarketplaceFilter);
        }

        filters.add(filter);

        userService.save(user);

        return OperationResult.ok().payload("user", userService.load(user.getId()));
    }
}
