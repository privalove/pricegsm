package com.pricegsm.securiry;

import com.pricegsm.domain.Administrator;
import com.pricegsm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractPrincipalHolder implements PrincipalHolder {

    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public boolean isAdmin() {
        return getCurrentUser() instanceof Administrator;
    }

    @Override
    public boolean isUser() {
        return getCurrentUser() instanceof User;
    }

    @Override
    public boolean isAuthorized() {
        return getCurrentUser() != null;
    }
}
