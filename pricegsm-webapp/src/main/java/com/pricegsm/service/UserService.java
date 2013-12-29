package com.pricegsm.service;

import com.pricegsm.dao.UserDao;
import com.pricegsm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;

@Service
public class UserService
        extends GlobalEntityService<User> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDao dao;

    @Override
    protected UserDao getDao() {
        return dao;
    }

    @Override
    protected User preSave(User entity) {
        entity.setPassword(encoder.encode(entity.getPassword()));

        return super.preSave(entity);
    }

    @Override
    protected void merge(User persisted, User entity) {
        super.merge(persisted, entity);

        entity.setPassword(persisted.getPassword());
        entity.setBalance(persisted.getBalance());
    }
}
