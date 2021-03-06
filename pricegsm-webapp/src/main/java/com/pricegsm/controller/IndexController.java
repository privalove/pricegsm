package com.pricegsm.controller;

import com.pricegsm.config.Build;
import com.pricegsm.config.PricegsmMessageSource;
import com.pricegsm.domain.*;
import com.pricegsm.domain.Currency;
import com.pricegsm.jackson.Wrappers;
import com.pricegsm.parser.PriceListDescriptor;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.*;
import com.pricegsm.util.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
    private VendorService vendorService;

    @Autowired
    private WorldPriceService worldPriceService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PricegsmMessageSource messageSource;

    @Autowired
    private PrincipalHolder principalHolder;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private Build build;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String layout(Locale locale, Model model) {

        if (principalHolder.isAdmin()) {
            return "redirect:/admin";
        }

        model.addAttribute("i18n", messageSource.getProperties(locale));
        model.addAttribute("profile", build.getProfile());

        return "fragments/layout";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return "index";
    }

    @RequestMapping(value = "/index.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult index(
            @RequestParam(value = "product") long productId,
            @RequestParam int currency,
            @RequestParam int dynRange,
            @RequestParam List<Long> types,
            @RequestParam int vendor,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date shopDate,
            @RequestParam String chartData) {

        Product selected = productService.load(productId);
        List<ProductPriceForm> prices = fetchPrices(vendor, types, dynRange, currency);
        OperationResult result = OperationResult.ok();

        if (!Utils.isEmpty(prices)) {
            boolean found = false;

            for (ProductPriceForm price : prices) {
                if (price.getProduct().equals(selected)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                selected = prices.get(0).getProduct();
            }

        }
        if (shopDate == null) {
            shopDate = Utils.yandexTime(yandexPriceService.findLastDate(productId));
        } else {
            shopDate = Utils.yandexTime(yandexPriceService.findLastDate(productId, Utils.today(shopDate), DateUtils.addDays(shopDate, 1)));
        }

        return result
                .payload("currency", currencyService.load(Math.min(currency, Currency.RUB)))
                .payload("courses", getCourses())
                .payload("dynRange", dynRange)
                .payload("shopDate", Wrappers.wrapDate(shopDate))
                .payload("chartData", chartData)
                .payload("product", selected)
                .payload("vendors", vendorService.findAll())
                .payload("vendorId", vendor)
                .payload("types", types)

                .payload("shopTime", shopDate)
                .payload("prices", prices)
                .payload("yandexPrices", fetchYandexPrices(selected, shopDate, currency))
                .payload("worldPrices", fetchWorldPrices(selected, currency))
                .payload("chart", fetchChart(selected, currency, chartData, dynRange));
    }

    @RequestMapping(value = "/index/chart.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult chart(
            @RequestParam(value = "product") long productId,
            @RequestParam int currency,
            @RequestParam String chartData,
            @RequestParam int dynRange) {

        Product selected = productService.load(productId);

        return OperationResult.ok()
                .payload("chart", fetchChart(selected, currency, chartData, dynRange));
    }

    @RequestMapping(value = "/index/shop.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult shop(
            @RequestParam(value = "product") long productId,
            @RequestParam int currency,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date shopDate) {

        Product selected = productService.load(productId);

        List<Map<String, Object>> yandexPrices = fetchYandexPrices(selected, shopDate, currency);
        List<Map<String, Object>> worldPrices = fetchWorldPrices(selected, currency);

        if (shopDate == null) {
            shopDate = Utils.yandexTime(yandexPriceService.findLastDate(productId));
        } else {
            shopDate = Utils.yandexTime(yandexPriceService.findLastDate(productId, Utils.today(shopDate), DateUtils.addDays(shopDate, 1)));
        }

        return OperationResult.ok()
                .payload("shopTime", shopDate)
                .payload("yandexPrices", yandexPrices)
                .payload("worldPrices", worldPrices);
    }

    private Map<String, Object> fetchChart(Product selected, int currency, String chartData, int chartRange) {
        Map<String, Object> result = new HashMap<>();

        Date to = DateUtils.addDays(Utils.today(), 1);
        Date from = DateUtils.addDays(to, -chartRange);

        List<Product> products = productService.findByYandexId(selected.getYandexId());

        List<Map<String, Object>> chart = new ArrayList<>();

        for (Product product : products) {


            Map<String, Object> data = new HashMap<>();
            chart.add(data);
            data.put("label", product.getColor().getName());
            data.put("color", product.getColor().getCode());
            data.put("points", Collections.singletonMap("show", true));
            data.put("lines", Collections.singletonMap("show", true));

            switch (chartData) {
                default:
                    data.put("data", yandexPriceService.getChartData(product.getId(), currency, from, to));
            }
        }

        result.put("data", chart);
        result.put("from", from);
        result.put("to", to);

        return result;
    }

    /**
     * [{
     * shop: "Price-Killers",
     * price: 768,
     * link: "http://price-killers.ru/iphone-5s-16gb"
     * }]
     */
    private List<Map<String, Object>> fetchYandexPrices(Product selected, Date shopDate, int currency) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<YandexPrice> prices = yandexPriceService.findByDateForProduct(selected.getId(), Utils.today(shopDate), DateUtils.addDays(shopDate, 1));

        for (YandexPrice price : prices) {
            Map<String, Object> map = new HashMap<>();
            map.put("shop", price.getShop());
            map.put("link", price.getLink());
            map.put("price", getPrice(price, currency));
            result.add(map);
        }

        return result;
    }

    private List<Map<String, Object>> fetchWorldPrices(
            Product selected, int currency) {
        List<WorldPrice> prices = new ArrayList<>();
        for (PriceListDescriptor descriptor : PriceListDescriptor.values()) {
            prices.addAll(worldPriceService.findByDateForProduct(selected.getId(), descriptor));
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (WorldPrice price : prices) {
            Map<String, Object> map = new HashMap<>();
            map.put("seller", price.getSeller());
            map.put("name", price.getPriceListProductName());
            map.put("description", price.getDescription());
            map.put("price", getPrice(price, currency));
            map.put("date", price.getDate());
            result.add(map);
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
    private List<ProductPriceForm> fetchPrices(int vendor, List<Long> types, int dynRange, int currency) {
        List<ProductPriceForm> result = new ArrayList<>();

        List<Product> products = productService.findActiveByVendorOrderByVendorAndName(vendor, types);

        String previousProductName = "";
        String previousType = "";
//        Map<Long, YandexPrice> productLastMinPricesMap =
//                yandexPriceService.getProductLastMinPricesMap(vendor);

        for (Product product : products) {

            YandexPrice yandexPrice = yandexPriceService.findLastMinPrice(product.getId());
//            YandexPrice yandexPrice = productLastMinPricesMap.get(product.getId());

            WorldPrice worldPrice = worldPriceService.findLast(product.getId());

            BigDecimal retail = getPrice(yandexPrice, currency);
            BigDecimal retailDelta = BigDecimal.ZERO;

            BigDecimal world = getPrice(worldPrice, currency);
            BigDecimal worldDelta = BigDecimal.ZERO;

            int count = 0;

            if (yandexPrice != null) {
                BigDecimal oldPrice = getPrice(yandexPriceService.findByDateMinPrice(product.getId(), DateUtils.addDays(yandexPrice.getDate(), -dynRange)), currency);
                if (!oldPrice.equals(BigDecimal.ZERO)) {
                    retailDelta = retail.subtract(oldPrice);
                }
                count = yandexPrice.getCount();
            }

            if (worldPrice != null) {
                BigDecimal oldPrice = getPrice(worldPriceService.findByDate(product.getId(), DateUtils.addDays(worldPrice.getDate(), -dynRange)), currency);
                if (!oldPrice.equals(BigDecimal.ZERO)) {
                    worldDelta = world.subtract(oldPrice);
                }
            }

            result.add(new ProductPriceForm(product, previousProductName, previousType, retail, retailDelta, count, BigDecimal.ZERO, BigDecimal.ZERO, world, worldDelta));
            previousProductName = product.getName();
            previousType = product.getType().getName();
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

    private List<BigDecimal> getCourses() {
        List<Exchange> lastExchanges = exchangeService.findLast();
        List<BigDecimal> lastCourses = new ArrayList<>();
        for (Exchange exchange : lastExchanges) {
            lastCourses.add(exchange.getValue());
        }
        return lastCourses;
    }
}
