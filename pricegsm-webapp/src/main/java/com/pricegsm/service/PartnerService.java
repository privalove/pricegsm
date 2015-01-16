package com.pricegsm.service;

import com.pricegsm.dao.PartnerDao;
import com.pricegsm.domain.Partner;
import com.pricegsm.domain.User;
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

    public Partner addPartnership(User userNewPartner) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
