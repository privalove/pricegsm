package com.pricegsm.service;

import com.pricegsm.dao.WorldPriceDao;
import com.pricegsm.domain.WorldPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldPriceService
        extends GlobalEntityService<WorldPrice> {

    @Autowired
    private WorldPriceDao dao;

    @Override
    protected WorldPriceDao getDao() {
        return dao;
    }
}
