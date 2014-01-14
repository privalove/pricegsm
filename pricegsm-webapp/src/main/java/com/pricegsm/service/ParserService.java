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


    @Scheduled(cron = "0 1,10,20 10,14,18 * * ?")
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
                        String url = AppSettings.getParserUrl() + "/yandex?yandexId=" + yandexId;

                        for (Product color : colors) {
                            url += "&color=" + color.getColor().getYandexColor();
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
                                        if (yandexPrice.getColor().equalsIgnoreCase(color.getColor().getYandexColor())
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
                                        logger.warn(Throwables.getRootCause(e).getMessage(), e);
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

    //every day at 10:00, 14:00, 18:00
    @Scheduled(cron = "0 56,57,58 9,13,17 * * ?")
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


                Date date = new Date(data.getJSONObject("USD_RUB").getLong("time"));

                if (last == null || date.after(last.getDate())) {

                    double usdRate = data.getJSONObject("USD_RUB").getDouble("value");
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
