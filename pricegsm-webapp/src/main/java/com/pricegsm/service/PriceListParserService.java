package com.pricegsm.service;

import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Exchange;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.WorldPrice;
import com.pricegsm.parser.PriceListExcelParser;
import com.pricegsm.parser.PriceListParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    public void parse(MultipartFile file) {
        List<Product> products = productService.findActive();

        PriceListParser parser = new PriceListExcelParser();

        List<WorldPrice> prices = parser.parse(getFile(file), products);

        Exchange rub = exchangeService.getLast(Currency.USD, Currency.RUB);
        Exchange eur = exchangeService.getLast(Currency.EUR, Currency.USD);

        for (WorldPrice price : prices) {
            BigDecimal priceUsd = price.getPriceUsd();
            price.setPriceRub(priceUsd.multiply(rub.getValue()));
            price.setPriceEur(priceUsd.multiply(eur.getValue()));
            price.setDate(new Date());

            worldPriceService.save(price);
        }

    }

    private File getFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File resultFile = new File(file.getName());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(resultFile));
                stream.write(bytes);
                stream.close();
                return resultFile;
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
        return null;
    }
}
