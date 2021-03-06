package com.pricegsm.service;

import com.pricegsm.dao.BaseUserDao;
import com.pricegsm.domain.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("baseUserService")
public class BaseUserServiceImpl
        extends GlobalEntityService<BaseUser>
        implements BaseUserService {

    @Autowired
    private BaseUserDao dao;

    @Override
    protected BaseUserDao getDao() {
        return dao;
    }

    @Override
    public BaseUser loadUserByUsername(String username)
            throws UsernameNotFoundException {
        BaseUser baseUser = getDao().loadByEmail(username);

        if (baseUser == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return baseUser;
    }
}
