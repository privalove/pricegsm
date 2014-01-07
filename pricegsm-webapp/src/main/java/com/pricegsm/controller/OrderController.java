package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Заказы
 */
@Controller
public class OrderController {

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public void order() {
    }

}
