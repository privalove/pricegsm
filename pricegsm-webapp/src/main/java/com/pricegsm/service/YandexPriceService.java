package com.pricegsm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.pricegsm.dao.ExchangeDao;
import com.pricegsm.dao.ProductDao;
import com.pricegsm.dao.YandexPriceDao;
import com.pricegsm.domain.*;
import com.pricegsm.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class YandexPriceService
        extends GlobalEntityService<YandexPrice> {

    @Autowired
    private YandexPriceDao dao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ExchangeDao exchangeDao;

    @Autowired
    @Lazy
    private ObjectMapper objectMapper;

    @Override
    protected YandexPriceDao getDao() {
        return dao;
    }

    @Scheduled(cron = "0 1,3,5 10,14,18 * * ?")
    public void readYandexData() throws IOException {
        List<Object[]> dates = getDao().findLast();

        if (!Utils.isEmpty(dates)) {

            Date yandexTime = Utils.yandexTime();
            Exchange usd = exchangeDao.getLast(Currency.USD, Currency.RUB);
            Exchange eur = exchangeDao.getLast(Currency.EUR, Currency.RUB);

            for (Object[] pair : dates) {
                Long productId = (Long) pair[0];
                Date date = (Date) pair[1];
                Product product = productDao.load(productId);
                List<Product> colors = productDao.findByYandexId(product.getYandexId());

                try {

                    if (date == null || date.before(yandexTime)) {
                        String url = AppSettings.getParserUrl() + "/yandex?yandexId=" + product.getYandexId();

                        for (Product color : colors) {
                            url += "&color=" + color.getColor().getYandexColor();
                        }

                        YandexParserResult response = objectMapper.readValue(Utils.readFromUrl(url), YandexParserResult.class);

                        if (response.getError() == 0) {
                            for (YandexPrice yandexPrice : response.getOffers()) {
                                yandexPrice.setDate(yandexTime);

                                yandexPrice.setPriceRub(yandexPrice.getPrice());
                                yandexPrice.setPriceUsd(yandexPrice.getPrice().divide(usd.getValue()));
                                yandexPrice.setPriceRub(yandexPrice.getPrice().divide(eur.getValue()));

                                for (Product color : colors) {
                                    if (yandexPrice.getColor().equalsIgnoreCase(color.getColor().getYandexColor())) {
                                        yandexPrice.setProduct(color);
                                        break;
                                    }
                                }

                                try {
                                    save(yandexPrice);
                                } catch (Exception e) {
                                    logger.warn("Error save yandex data for product {}: {}", yandexPrice.getProduct().getId(), Throwables.getRootCause(e));
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.warn("Error parse yandex data for product {}: {}", product.getId(), Throwables.getRootCause(e));
                }
            }

        }

    }
}
