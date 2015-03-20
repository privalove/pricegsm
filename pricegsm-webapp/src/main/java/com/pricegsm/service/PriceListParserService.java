package com.pricegsm.service;

import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Exchange;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.WorldPrice;
import com.pricegsm.parser.PriceListExcelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * User: o.logunov
 * Date: 13.02.15
 * Time: 0:26
 */
@Service
public class PriceListParserService {

    @Autowired
    private WorldPriceService worldPriceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ExchangeService exchangeService;

    public void parse(MultipartFile file, String sellerName) {
        List<Product> products = productService.findActive();

        PriceListExcelParser parser = new PriceListExcelParser(sellerName);

        List<WorldPrice> prices = parser.parse(file, products);

        Exchange rub = exchangeService.getLast(Currency.USD, Currency.RUB);
        Exchange eur = exchangeService.getLast(Currency.EUR, Currency.USD);

        Date date = new Date();
        for (WorldPrice price : prices) {
            BigDecimal priceUsd = price.getPriceUsd();
            price.setPriceRub(priceUsd.multiply(rub.getValue()).setScale(0, RoundingMode.HALF_UP));
            price.setPriceEur(priceUsd.divide(eur.getValue(), RoundingMode.HALF_UP));
            price.setDate(date);

            worldPriceService.save(price);
        }

    }
}
