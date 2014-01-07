package com.pricegsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Контрагенты
 */
@Controller
public class PartnerController {
    @RequestMapping(value = "/partner", method = RequestMethod.GET)
    public void partner() {
    }

}
