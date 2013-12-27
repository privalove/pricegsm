package com.pricegsm.service;

import com.pricegsm.dao.AdministratorDao;
import com.pricegsm.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService
        extends GlobalEntityService<Administrator> {

    @Autowired
    private AdministratorDao dao;

    @Override
    protected AdministratorDao getDao() {
        return dao;
    }
}
