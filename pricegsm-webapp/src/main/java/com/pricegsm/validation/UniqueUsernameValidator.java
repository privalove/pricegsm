package com.pricegsm.validation;

import com.pricegsm.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator
        implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private BaseUserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            userService.loadUserByUsername(value);
            return false;
        } catch (UsernameNotFoundException e) {
            return true;
        }

    }
}
