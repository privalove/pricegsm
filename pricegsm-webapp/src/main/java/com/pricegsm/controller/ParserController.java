package com.pricegsm.controller;

import com.pricegsm.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParserController {

    @Autowired
    private ParserService parserService;

    @RequestMapping(value = "/yandex", method = RequestMethod.GET)
    public void yandex() throws Exception {
        parserService.readYandexData();
    }

    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    public void exchange() throws Exception {
        parserService.readExchanges();
    }

}
