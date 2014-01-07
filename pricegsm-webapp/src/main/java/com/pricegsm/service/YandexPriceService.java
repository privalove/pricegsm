package com.pricegsm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.pricegsm.dao.ExchangeDao;
import com.pricegsm.dao.ProductDao;
import com.pricegsm.dao.YandexPriceDao;
import com.pricegsm.domain.*;
import com.pricegsm.util.ApplicationContextProvider;
import com.pricegsm.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class YandexPriceService
        extends GlobalEntityService<YandexPrice> {

    @Autowired
    private YandexPriceDao dao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ExchangeDao exchangeDao;

    @Override
    protected YandexPriceDao getDao() {
        return dao;
    }

    @Scheduled(cron = "0 1,3,5 10,14,18 * * ?")
    @Transactional
    public void readYandexData() throws IOException {
        List<Object[]> dates = getDao().findLast();

        if (!Utils.isEmpty(dates)) {

            Date yandexTime = Utils.yandexTime();
            Exchange usd = exchangeDao.getLast(Currency.USD, Currency.RUB);
            Exchange eur = exchangeDao.getLast(Currency.EUR, Currency.RUB);

            for (Object[] pair : dates) {
                String yandexId = (String) pair[0];
                Date date = (Date) pair[1];
                List<Product> colors = productDao.findByYandexId(yandexId);

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
                                    save(yandexPrice);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.warn("Error parse yandex data for product {}: {}", yandexId, Throwables.getRootCause(e));
                    logger.warn(e.getMessage(), e);
                }
            }

        }

    }

    private ObjectMapper getObjectMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(ObjectMapper.class);
    }

    public Date findLastDate(long productId) {
        return getDao().findLastDate(productId);
    }

    public YandexPrice findLastMinPrice(long productId) {
        return postLoad(getDao().findLastMinPrice(productId));
    }

    public YandexPrice findByDateMinPrice(long productId, Date date) {
        return postLoad(getDao().findByDateMinPrice(productId, date));
    }

    public List<YandexPrice> findByDateForProducts(Date date, List<Product> products) {
        return postLoad(getDao().findByDateForProducts(date, products));
    }
}
