package com.pricegsm.service;

import com.pricegsm.dao.ExchangeDao;
import com.pricegsm.domain.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService
        extends GlobalEntityService<Exchange> {

    @Autowired
    private ExchangeDao dao;

    @Override
    protected ExchangeDao getDao() {
        return dao;
    }
}
