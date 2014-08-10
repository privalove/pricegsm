package com.pricegsm.config;

import com.pricegsm.domain.BaseUser;
import com.pricegsm.domain.User;
import com.pricegsm.securiry.AbstractPrincipalHolder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: pu6istiyKomp
 * Date: 10.05.14
 * Time: 22:41
 */
@Component
@Profile("test")
public class TestPrincipalHolder extends AbstractPrincipalHolder {
    @Override
    public BaseUser getCurrentUser() {
        User user = new User();
        user.setId(10001L);
        return user;
    }
}
