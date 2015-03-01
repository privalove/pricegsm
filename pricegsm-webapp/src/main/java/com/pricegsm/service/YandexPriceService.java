package com.pricegsm.service;

import com.pricegsm.dao.YandexPriceDao;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.YandexPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Date findLastDate(long productId, Date from, Date to) {
        return getDao().findLastDate(productId, from, to);
    }

    public YandexPrice findLastMinPrice(long productId) {
        return postLoad(getDao().findLastMinPrice(productId));
    }

    public Map<Long, YandexPrice> getProductLastMinPricesMap(long vendorId) {

        List<YandexPrice> yandexPrices = getDao().findLastMinPrices(vendorId);
        Map<Long, YandexPrice> result = new HashMap<>();
        for (YandexPrice yandexPrice : yandexPrices) {
            result.put(yandexPrice.getProduct().getId(), yandexPrice);
        }

        return result;
    }

    public YandexPrice findByDateMinPrice(long productId, Date date) {
        return postLoad(getDao().findByDateMinPrice(productId, date));
    }

    public List<YandexPrice> findByDateForProduct(long productId, Date from, Date to) {
        return postLoad(getDao().findByDateForProduct(productId, from, to));
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

    /**
     * @return List of pairs {Product ID, Date}
     */
    public List<Object[]> findLastByColors() {
        return getDao().findLastByColors();
    }

}
