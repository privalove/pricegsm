package com.pricegsm.securiry;

import com.pricegsm.domain.BaseUser;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class DefaultPrincipalHolder extends AbstractPrincipalHolder {

    @Override
    public BaseUser getCurrentUser() {
        Object principal = null;
        SecurityContext context = SecurityContextHolder.getContext();
        //check if security enabled
        if (context != null) {
            //check if authenticated
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                principal = authentication.getPrincipal();
            }
        }
        return (principal instanceof BaseUser) ? (BaseUser) principal : null;
    }


}
