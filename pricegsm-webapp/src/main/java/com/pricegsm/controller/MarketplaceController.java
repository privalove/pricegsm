package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Торговая площадка
 *
 * @author andreybugaev
 * @since 25.12.2013
 */
@Controller
public class MarketplaceController {

    @RequestMapping(value = "/marketplace", method = RequestMethod.GET)
    public void marketplace() {
    }
}
