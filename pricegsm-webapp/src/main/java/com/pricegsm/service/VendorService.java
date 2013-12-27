package com.pricegsm.service;

import com.pricegsm.dao.VendorDao;
import com.pricegsm.domain.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService
        extends GlobalEntityService<Vendor> {

    @Autowired
    private VendorDao dao;

    @Override
    protected VendorDao getDao() {
        return dao;
    }
}
