package com.pricegsm.controller;

import com.pricegsm.config.PricegsmMessageSource;
import com.pricegsm.domain.*;
import com.pricegsm.domain.Currency;
import com.pricegsm.service.ProductService;
import com.pricegsm.service.WorldPriceService;
import com.pricegsm.service.YandexPriceService;
import com.pricegsm.util.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private PricegsmMessageSource messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String layout(Locale locale, Model model) {

        model.addAttribute("i18n", messageSource.getProperties(locale));

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
            @RequestParam(required = false) @DateTimeFormat(pattern="dd.MM.yyyy") Date shopDate,
            @RequestParam(defaultValue = "retail") String chartData,
            @RequestParam(defaultValue = "7") int chartRange) {

        Product selected = productService.load(productId);

        if (shopDate == null) {
            shopDate = yandexPriceService.findLastDate(productId);
        }
        if (shopDate == null) {
            shopDate = new Date();
        }

        return OperationResult.ok()
                .payload("currency", Math.min(currency, Currency.RUB))
                .payload("dynRange", dynRange)
                .payload("shopDate", shopDate)
                .payload("chartData", chartData)
                .payload("chartRange", chartRange)
                .payload("product", selected)
                .payload("vendor", selected.getVendor())

                .payload("prices", fetchPrices(dynRange, currency))
                .payload("colors", fetchColors(selected))
                .payload("yandexPrices", fetchYandexPrices(selected, shopDate, currency))
                .payload("chart", fetchChart(selected, currency, chartData, chartRange));
    }

    @RequestMapping(value = "/index/chart.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public OperationResult chart(
            @RequestParam(value = "product", defaultValue = "1") long productId,
            @RequestParam(defaultValue = "1") int currency,
            @RequestParam(defaultValue = "retail") String chartData,
            @RequestParam(defaultValue = "7") int chartRange) {

        Product selected = productService.load(productId);

        return OperationResult.ok()
                .payload("currency", Math.min(currency, Currency.RUB))
                .payload("chartData", chartData)
                .payload("chartRange", chartRange)
                .payload("product", selected)
                .payload("vendor", selected.getVendor())

                .payload("chart", fetchChart(selected, currency, chartData, chartRange));
    }

    @RequestMapping(value = "/index/price.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public OperationResult price(
            @RequestParam(value = "product", defaultValue = "1") long productId,
            @RequestParam(defaultValue = "7") int dynRange,
            @RequestParam(defaultValue = "1") int currency) {

        Product selected = productService.load(productId);

        return OperationResult.ok()
                .payload("currency", Math.min(currency, Currency.RUB))
                .payload("dynRange", dynRange)
                .payload("product", selected)
                .payload("vendor", selected.getVendor())

                .payload("prices", fetchPrices(dynRange, currency));
    }

    @RequestMapping(value = "/index/shop.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public OperationResult shop(
            @RequestParam(value = "product", defaultValue = "1") long productId,
            @RequestParam(defaultValue = "1") int currency,
            @RequestParam(required = false) @DateTimeFormat(pattern="dd.MM.yyyy") Date shopDate) {

        Product selected = productService.load(productId);

        if (shopDate == null) {
            shopDate = yandexPriceService.findLastDate(productId);
        }
        if (shopDate == null) {
            shopDate = new Date();
        }

        return OperationResult.ok()
                .payload("currency", Math.min(currency, Currency.RUB))
                .payload("shopDate", shopDate)
                .payload("product", selected)
                .payload("vendor", selected.getVendor())

                .payload("colors", fetchColors(selected))
                .payload("yandexPrices", fetchYandexPrices(selected, shopDate, currency));
    }


    private List<Color> fetchColors(Product selected) {
        return productService.findColors(selected.getYandexId());
    }

    private Map<String, Object> fetchChart(Product selected, int currency, String chartData, int chartRange) {
        Map<String, Object> result = new HashMap<>();

        Date to = DateUtils.addDays(Utils.today(), 1);
        Date from =  DateUtils.addDays(to, -chartRange);

        List<Product> products = productService.findByYandexId(selected.getYandexId());

        Map<String, Object> data = new HashMap<>();

        for (Product product : products) {
            switch (chartData) {
                default: data.put(product.getColor().getName(), yandexPriceService.getChartData(product.getId(), currency, from, to));
            }
        }

        result.put("data", data);
        result.put("from", from);
        result.put("to", to);

        return result;
    }

    /**
     * [{
     * shop: "Price-Killers",
     * link: "http://price-killers.ru/iphone-5s-16gb",
     * Gold: 768,
     * Space Gray: 771,
     * Silver: 731
     * }]
     */
    private List<Map<String, Object>> fetchYandexPrices(Product selected, Date shopDate, int currency) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<Product> products = productService.findByYandexId(selected.getYandexId());

        List<YandexPrice> prices = yandexPriceService.findByDateForProducts(DateUtils.addDays(shopDate, 1), products);

        Map<String, Map<String, Object>> unique = new HashMap<>();

        for (YandexPrice price : prices) {
            Map<String, Object> map = unique.get(price.getShop());
            if (map == null) {
                map = new HashMap<>();
                map.put("shop", price.getShop());
                map.put("link", price.getLink());
                result.add(map);
                unique.put(price.getShop(), map);
            }

            String name = "id" + price.getProduct().getColor().getId();
            map.put(name, getPrice(price, currency));
        }

        return result;
    }

    /**
     * [{
     * id: 14,
     * product: "iPhone 5C 16Gb",
     * color: "Yellow",
     * vendor: "Apple IPhone",
     * retail: 593,
     * retailDelta: -5,
     * opt: 550,
     * optDelta: -7,
     * world: 500,
     * worldDelta: -20
     * }]
     */
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
