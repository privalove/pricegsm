package com.pricegsm.controller;

import com.pricegsm.service.ExchangeService;
import com.pricegsm.service.YandexPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParserController {

    @Autowired
    private YandexPriceService yandexPriceService;

    @Autowired
    private ExchangeService exchangeService;

    @RequestMapping(value = "/yandex", method = RequestMethod.GET)
    public void yandex() throws Exception {
        yandexPriceService.readYandexData();
    }

    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    public void exchange() throws Exception {
        exchangeService.readExchanges();
    }

}
