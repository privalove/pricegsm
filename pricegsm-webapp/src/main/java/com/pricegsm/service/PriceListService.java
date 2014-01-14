package com.pricegsm.service;

import com.pricegsm.dao.PriceListDao;
import com.pricegsm.domain.PriceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceListService
        extends GlobalEntityService<PriceList> {

    @Autowired
    private PriceListDao dao;

    @Override
    protected PriceListDao getDao() {
        return dao;
    }

    public List<PriceList> findAllForCurrentUser() {
        return getDao().findForUser(principalHolder.getCurrentUser().getId());
    }
}
