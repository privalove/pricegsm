package com.pricegsm.service;

import com.pricegsm.dao.WorldPriceDao;
import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorldPriceService
        extends GlobalEntityService<WorldPrice> {

    @Autowired
    private WorldPriceDao dao;

    @Override
    protected WorldPriceDao getDao() {
        return dao;
    }

    public WorldPrice findLast(long productId) {
        return getDao().findLast(productId);
    }

    public WorldPrice findByDate(long productId, Date date) {
        return getDao().findByDate(productId, date);
    }

    public List<WorldPrice> findByDateForProduct(long productId, Date from, Date to) {
        return postLoad(getDao().findByDateForProduct(productId, from, to));
    }
}
