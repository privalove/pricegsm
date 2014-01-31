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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return postLoad(getDao().getLast(from, to));
    }

    public List<Exchange> findLast() {
        List<Exchange> result = new ArrayList<>();

        Exchange e = getDao().getLast(Currency.USD, Currency.RUB);
        if (e != null) {
            result.add(e);
        }
        e = getDao().getLast(Currency.EUR, Currency.RUB);
        if (e != null) {
            result.add(e);
        }
        e = getDao().getLast(Currency.EUR, Currency.USD);
        if (e != null) {
            result.add(e);
        }


        return postLoad(result);
    }

}

