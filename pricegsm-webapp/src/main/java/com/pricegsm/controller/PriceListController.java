package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Прайслист
 */
@Controller
public class PriceListController {
    @RequestMapping(value = "/price_list", method = RequestMethod.GET)
    public String marketplace() {
        return "price_list";
    }

}
