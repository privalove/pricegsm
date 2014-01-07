package com.pricegsm.controller;

import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;
import com.pricegsm.service.ProductService;
import com.pricegsm.service.WorldPriceService;
import com.pricegsm.service.YandexPriceService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;

/**
 * Главная страница
 */
@Controller
public class IndexController {

    @Autowired
    private YandexPriceService yandexPriceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorldPriceService worldPriceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String layout() {
        return "fragments/layout";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index() {
    }

    @RequestMapping(value = "/index.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public OperationResult index(
            @RequestParam(value = "product", defaultValue = "1") long productId,
            @RequestParam(defaultValue = "1") int currency,
            @RequestParam(defaultValue = "7") int dynRange,
            @RequestParam(required = false) Date shopDate,
            @RequestParam(defaultValue = "retail") String chartData,
            @RequestParam(defaultValue = "7") int chartRange) {

        Product selected = productService.load(productId);

        if (shopDate == null) {
            shopDate = yandexPriceService.findLastDate(productId);
        }


        return OperationResult.ok()
                .payload("currency", Math.min(currency, Currency.RUB))
                .payload("dynRange", dynRange)
                .payload("shopDate", shopDate)
                .payload("chartData", chartData)
                .payload("chartRange", chartRange)

                .payload("prices", fetchPrices(dynRange, currency))
                .payload("yandexPrices", fetchYandexPrices(selected, shopDate))
                .payload("chart", fetchChart(selected, chartData, chartRange))
                .payload("product", selected);
    }

    private Map<String, Object> fetchChart(Product selected, String chartData, int chartRange) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }

    private List<Map<String, Object>> fetchYandexPrices(Product selected, Date shopDate) {
        List<Map<String, Object>> result = new ArrayList<>();

        return result;
    }

    private List<ProductPriceForm> fetchPrices(int dynRange, int currency) {
        List<ProductPriceForm> result = new ArrayList<>();

        List<Product> products = productService.findActiveOrderByVendorAndName();

        for (Product product : products) {

            YandexPrice yandexPrice = yandexPriceService.findLastMinPrice(product.getId());
            WorldPrice worldPrice = worldPriceService.findLast(product.getId());

            BigDecimal retail = getPrice(yandexPrice, currency);
            BigDecimal retailDelta = BigDecimal.ZERO;

            BigDecimal world = getPrice(worldPrice, currency);
            BigDecimal worldDelta = BigDecimal.ZERO;

            if (yandexPrice != null) {
                BigDecimal oldPrice = getPrice(yandexPriceService.findByDateMinPrice(product.getId(), DateUtils.addDays(yandexPrice.getDate(), -dynRange)), currency);
                if (!oldPrice.equals(BigDecimal.ZERO)) {
                    retailDelta = retail.subtract(oldPrice);
                }
            }

            if (worldPrice != null) {
                BigDecimal oldPrice = getPrice(worldPriceService.findByDate(product.getId(), DateUtils.addDays(worldPrice.getDate(), -dynRange)), currency);
                if (!oldPrice.equals(BigDecimal.ZERO)) {
                    worldDelta = world.subtract(oldPrice);
                }
            }

            result.add(new ProductPriceForm(product, retail, retailDelta, BigDecimal.ZERO, BigDecimal.ZERO, world, worldDelta));

        }

        return result;
    }

    private BigDecimal getPrice(YandexPrice yandexPrice, int currency) {
        if (yandexPrice == null) {
            return BigDecimal.ZERO;
        }

        switch (currency) {
            case (int) Currency.EUR:
                return yandexPrice.getPriceEur();
            case (int) Currency.USD:
                return yandexPrice.getPriceUsd();
            default:
                return yandexPrice.getPriceRub();
        }
    }

    private BigDecimal getPrice(WorldPrice yandexPrice, int currency) {
        if (yandexPrice == null) {
            return BigDecimal.ZERO;
        }

        switch (currency) {
            case (int) Currency.EUR:
                return yandexPrice.getPriceEur();
            case (int) Currency.USD:
                return yandexPrice.getPriceUsd();
            default:
                return yandexPrice.getPriceRub();
        }
    }

}
