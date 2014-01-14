package com.pricegsm.controller;

import com.pricegsm.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Прайслист
 */
@Controller
public class PriceListController {

    @Autowired
    private PriceListService priceListService;

    @RequestMapping(value = "/price_list", method = RequestMethod.GET)
    public String priceList() {
        return "price_list";
    }

    @RequestMapping(value = "/price_list.json", method = RequestMethod.GET, produces = "application/json")
    public OperationResult priceListResource() {
        return OperationResult.ok()
                .payload("priceLists", priceListService.findAllForCurrentUser());
    }


}
