package com.pricegsm.service;

import com.pricegsm.dao.UserDao;
import com.pricegsm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
        extends GlobalEntityService<User> {

    @Autowired
    private UserDao dao;

    @Override
    protected UserDao getDao() {
        return dao;
    }
}
