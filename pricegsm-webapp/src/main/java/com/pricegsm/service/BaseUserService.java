package com.pricegsm.service;

import com.pricegsm.dao.BaseUserDao;
import com.pricegsm.domain.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService
        extends GlobalEntityService<BaseUser>
        implements UserDetailsService {

    @Autowired
    private BaseUserDao dao;

    @Override
    protected BaseUserDao getDao() {
        return dao;
    }

    @Override
    public BaseUser loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return getDao().loadByEmail(username);
    }
}
