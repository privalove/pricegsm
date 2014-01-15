package com.pricegsm.validation;

import com.pricegsm.controller.ChangePasswordForm;
import com.pricegsm.controller.RegistrationForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordValidator
        implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.isAssignableFrom(clazz.getClass())
                || ChangePasswordForm.class.isAssignableFrom(clazz.getClass());
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (target instanceof RegistrationForm) {
            RegistrationForm password = (RegistrationForm) target;

            if (!password.getPassword().equals(password.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "error.NotEquals");
            }
        }

        if (target instanceof ChangePasswordForm) {
            ChangePasswordForm password = (ChangePasswordForm) target;

            if (!password.getPassword().equals(password.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "error.NotEquals");
            }
        }

    }
}
