package com.pricegsm.service;

import com.pricegsm.dao.CurrencyDao;
import com.pricegsm.domain.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService
        extends GlobalEntityService<Currency> {

    @Autowired
    private CurrencyDao dao;

    @Override
    protected CurrencyDao getDao() {
        return dao;
    }
}
