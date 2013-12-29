package com.pricegsm.securiry;

import com.pricegsm.domain.BaseUser;


public interface PrincipalHolder {

    BaseUser getCurrentUser();

    boolean isAdmin();

    boolean isUser();

    boolean isAuthorized();

}
