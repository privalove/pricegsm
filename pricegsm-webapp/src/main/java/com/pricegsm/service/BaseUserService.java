package com.pricegsm.service;

import com.pricegsm.dao.BaseUserDao;
import com.pricegsm.domain.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService
        extends GlobalEntityService<BaseUser> {

    @Autowired
    private BaseUserDao dao;

    @Override
    protected BaseUserDao getDao() {
        return dao;
    }
}
