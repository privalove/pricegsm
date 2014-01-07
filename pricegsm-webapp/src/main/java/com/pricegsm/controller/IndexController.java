package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Главная страница
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String layout() {
        return "fragments/layout";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index() {
    }
}
