package com.pricegsm.service;

import com.pricegsm.dao.PartnerDao;
import com.pricegsm.domain.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerService
        extends GlobalEntityService<Partner> {

    @Autowired
    private PartnerDao dao;

    @Override
    protected PartnerDao getDao() {
        return dao;
    }
}
