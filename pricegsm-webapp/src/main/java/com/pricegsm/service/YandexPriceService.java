package com.pricegsm.service;

import com.pricegsm.dao.YandexPriceDao;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.YandexPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class YandexPriceService
        extends GlobalEntityService<YandexPrice> {

    @Autowired
    private YandexPriceDao dao;

    @Override
    protected YandexPriceDao getDao() {
        return dao;
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

    public List<Object[]> getChartData(long productId, int currency, Date from, Date to) {
        return getDao().getChartData(productId, currency, from, to);
    }

    /**
     * @return List of pairs {Product Yandex ID, Date}
     */
    public List<Object[]> findLast() {
        return getDao().findLast();
    }
}
