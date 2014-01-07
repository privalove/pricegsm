package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Продажи
 */
@Controller
public class SalesController {

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    public void sales() {
    }

}
