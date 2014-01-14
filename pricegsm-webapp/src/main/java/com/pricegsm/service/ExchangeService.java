package com.pricegsm.service;

import com.pricegsm.dao.CurrencyDao;
import com.pricegsm.dao.ExchangeDao;
import com.pricegsm.domain.AppSettings;
import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Exchange;
import com.pricegsm.util.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExchangeService
        extends GlobalEntityService<Exchange> {

    @Autowired
    private ExchangeDao dao;

    @Override
    protected ExchangeDao getDao() {
        return dao;
    }

    public Exchange getLast(long from, long to) {
        return getDao().getLast(from, to);
    }
}

