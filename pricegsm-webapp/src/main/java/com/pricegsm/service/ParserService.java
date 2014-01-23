package com.pricegsm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.pricegsm.domain.*;
import com.pricegsm.util.ApplicationContextProvider;
import com.pricegsm.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ParserService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private YandexPriceService yandexPriceService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ProductService productService;

    /**
     * <pre>
     * {"result": {
     *      "offers": [{
     *          "price": 22960,
     *          "shop": "smarto-msk",
     *          "link": "http://smarto-msk.ru?productId=123",
     *          "position": 0,
     *      }, {
     *          "price": 23189,
     *          "shop": "MobiAmo",
     *          "link": "http://MobiAmo.com?productId=123",
     *          "position": 0,
     *      }],
     *      "error":0
     * }}
     * </pre>
     */
    @Scheduled(cron = "0 15,35,55 10,14,18 * * ?")
    public void readYandexData2() throws IOException {
        List<Object[]> dates = yandexPriceService.findLastByColors();

        if (!Utils.isEmpty(dates)) {

            Date yandexTime = Utils.yandexTime();
            Exchange usd = exchangeService.getLast(Currency.USD, Currency.RUB);
            Exchange eur = exchangeService.getLast(Currency.EUR, Currency.RUB);

            for (Object[] pair : dates) {
                Long productId = (Long) pair[0];
                Date date = (Date) pair[1];
                Product product = productService.load(productId);

                try {

                    if (date == null || date.before(yandexTime)) {

                        String search = "(" + product.getSearchQuery().replaceAll(",", "|") + ")";
                        String color = !Utils.isEmpty(product.getColor().getYandexColor())
                                ? "(" + product.getColor().getYandexColor().replaceAll(",", "|") + ")"
                                : "";
                        String exclude = !Utils.isEmpty(product.getExcludeQuery())
                                ? "~(" + product.getExcludeQuery().replaceAll(",", "|") + ")"
                                : "";
                        String query = search + color + exclude;

                        String url = AppSettings.getParserUrl() + "/yandex2?query=" + URLEncoder.encode(query, "UTF-8") + "&yandexTypeId=" + product.getType().getYandexId();

                        logger.info("Fetch url: {}", url);

                        JSONObject response = Utils.readJsonFromUrl(url);
                        JSONObject result = response.getJSONObject("result");

                        if (result != null) {

                            int error = result.getInt("error");

                            if (error == 0) {
                                JSONArray offers = result.getJSONArray("offers");


                                YandexPrice[] prices = getObjectMapper().readValue(offers.toString(), YandexPrice[].class);

                                for (YandexPrice yandexPrice : prices) {
                                    yandexPrice.setDate(yandexTime);

                                    yandexPrice.setPriceRub(yandexPrice.getPrice());
                                    yandexPrice.setPriceUsd(yandexPrice.getPrice().divide(usd.getValue(), RoundingMode.HALF_UP));
                                    yandexPrice.setPriceEur(yandexPrice.getPrice().divide(eur.getValue(), RoundingMode.HALF_UP));
                                    yandexPrice.setProduct(product);

                                }

                                for (YandexPrice yandexPrice : prices) {
                                    try {
                                        yandexPriceService.save(yandexPrice);
                                    } catch (Exception e) {
                                        logger.warn(Throwables.getRootCause(e).getMessage());
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.warn("Error parse yandex data for product {}: {}", productId, Throwables.getRootCause(e).getMessage());
                    logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
                }
            }

        }

    }

    /**
     * <pre>
     * {"result": {
     *      "offers": [{
     *          "price": 22960,
     *          "shop": "smarto-msk",
     *          "link": "http://smarto-msk.ru?productId=123",
     *          "position": 0,
     *          "color": 1
     *      }, {
     *          "price": 23189,
     *          "shop": "MobiAmo",
     *          "link": "http://MobiAmo.com?productId=123",
     *          "position": 0,
     *          "color": 1
     *      }],
     *      "error":0
     * }}
     * </pre>
     */
    public void readYandexData() throws IOException {
        List<Object[]> dates = yandexPriceService.findLast();

        if (!Utils.isEmpty(dates)) {

            Date yandexTime = Utils.yandexTime();
            Exchange usd = exchangeService.getLast(Currency.USD, Currency.RUB);
            Exchange eur = exchangeService.getLast(Currency.EUR, Currency.RUB);

            for (Object[] pair : dates) {
                String yandexId = (String) pair[0];
                Date date = (Date) pair[1];
                List<Product> colors = productService.findByYandexId(yandexId);

                try {

                    if (date == null || date.before(yandexTime)) {
                        String url = AppSettings.getParserUrl() + "/yandex?yandexId=" + yandexId + "&yandexTypeId=" + colors.get(0).getType().getYandexId();

                        for (Product color : colors) {
                            url += "&id" + color.getColor().getId() + "=" + URLEncoder.encode(color.getColor().getYandexColor(), "UTF-8");
                        }

                        logger.info("Fetch url: {}", url);

                        JSONObject response = Utils.readJsonFromUrl(url);
                        JSONObject result = response.getJSONObject("result");

                        if (result != null) {

                            int error = result.getInt("error");

                            //check unique constrain in set
                            Set<YandexPrice> set = new HashSet<>();

                            if (error == 0) {
                                JSONArray offers = result.getJSONArray("offers");


                                YandexPrice[] prices = getObjectMapper().readValue(offers.toString(), YandexPrice[].class);

                                for (YandexPrice yandexPrice : prices) {
                                    yandexPrice.setDate(yandexTime);

                                    yandexPrice.setPriceRub(yandexPrice.getPrice());
                                    yandexPrice.setPriceUsd(yandexPrice.getPrice().divide(usd.getValue(), RoundingMode.HALF_UP));
                                    yandexPrice.setPriceEur(yandexPrice.getPrice().divide(eur.getValue(), RoundingMode.HALF_UP));

                                    for (Product color : colors) {
                                        if (yandexPrice.getColor() == color.getColor().getId()
                                                && !set.contains(yandexPrice)) {
                                            yandexPrice.setProduct(color);
                                            set.add(yandexPrice);
                                            break;
                                        }
                                    }

                                }

                                for (YandexPrice yandexPrice : set) {
                                    try {
                                        yandexPriceService.save(yandexPrice);
                                    } catch (Exception e) {
                                        logger.warn(Throwables.getRootCause(e).getMessage());
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.warn("Error parse yandex data for product {}: {}", yandexId, Throwables.getRootCause(e).getMessage());
                    logger.warn(e.getMessage(), e);
                }
            }

        }

    }

    /**
     * Scheduled every day at 10:00, 14:00, 18:00
     * <p/>
     * <pre>
     * {"result": {
     *      "error": 0,
     *      "data": {
     *          "USD_RUB_2": {
     *              "date": "2014-01-14 14:08",
     *              "time": 1389697732251,
     *              "value": 33.28
     *          },
     *          "USD_RUB": {
     *              "date": "2014-01-14 14:08",
     *              "time": 1389697732251,
     *              "value": 33.287
     *          },
     *          "EUR_RUB": {
     *              "date": "2014-01-14 14:07",
     *              "time": 1389697672251,
     *              "value": 45.5005
     *          },
     *          "EUR_USD": {
     *              "date": "2014-01-14 13:59",
     *              "time": 1389697192251,
     *              "value": 1.3673
     *          }
     *      }
     * }}
     * </pre>
     */
    @Scheduled(cron = "0 10,11,12 10,14,18 * * ?")
    public void readExchanges() throws IOException {

        Exchange last = exchangeService.getLast(Currency.USD, Currency.RUB);
        Currency usd = currencyService.load(Currency.USD);
        Currency eur = currencyService.load(Currency.EUR);
        Currency rub = currencyService.load(Currency.RUB);

        String url = AppSettings.getParserUrl() + "/currency";

        logger.info("Fetch url: {}", url);


        JSONObject response = Utils.readJsonFromUrl(url);

        JSONObject result = response.getJSONObject("result");

        if (result != null) {

            int error = result.getInt("error");

            if (error == 0) {
                JSONObject data = result.getJSONObject("data");

                JSONObject usdRub = data.has("USD_RUB") ? data.getJSONObject("USD_RUB") : data.getJSONObject("USD_RUB_2");

                Date date = new Date(usdRub.getLong("time"));

                if (last == null || date.after(last.getDate())) {

                    double usdRate = usdRub.getDouble("value");
                    double eurRate = data.getJSONObject("EUR_RUB").getDouble("value");
                    double uerUsdRate = data.getJSONObject("EUR_USD").getDouble("value");
                    exchangeService.save(new Exchange(usd, rub, date, new BigDecimal(usdRate)));
                    exchangeService.save(new Exchange(eur, rub, date, new BigDecimal(eurRate)));
                    exchangeService.save(new Exchange(eur, usd, date, new BigDecimal(uerUsdRate)));
                    logger.info("Exchanges have been saved successfully");
                }
            }
        } else {
            logger.warn("Error fetching data");
        }

    }

    private ObjectMapper getObjectMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(ObjectMapper.class);
    }
}
