package com.pricegsm.service;

import com.pricegsm.dao.YandexPriceDao;
import com.pricegsm.domain.YandexPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YandexPriceService
        extends GlobalEntityService<YandexPrice> {

    @Autowired
    private YandexPriceDao dao;

    @Override
    protected YandexPriceDao getDao() {
        return dao;
    }
}
