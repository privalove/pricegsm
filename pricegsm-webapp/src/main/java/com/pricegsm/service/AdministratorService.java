package com.pricegsm.service;

import com.pricegsm.controller.admin.ProfileForm;
import com.pricegsm.dao.AdministratorDao;
import com.pricegsm.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdministratorService
        extends GlobalEntityService<Administrator> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdministratorDao dao;

    @Override
    protected AdministratorDao getDao() {
        return dao;
    }

    @Transactional
    public Administrator updateProfile(ProfileForm form) {
        Administrator user = getDao().load(principalHolder.getCurrentUser().getId());
        form.fill(user);
        return getDao().merge(user);
    }

    @Transactional
    public Administrator changePassword(String password) {
        Administrator user = getDao().load(principalHolder.getCurrentUser().getId());
        user.setPassword(encoder.encode(password));
        return getDao().merge(user);
    }

}
