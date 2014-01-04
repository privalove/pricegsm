package com.pricegsm.controller;

import com.pricegsm.domain.User;
import com.pricegsm.validation.UniqueUsername;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegistrationForm
        implements Serializable {

    private String name;

    private String email;

    private String password;

    private String confirmPassword;

    @NotBlank
    @Size(max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @UniqueUsername
    @Email
    @NotBlank
    @Size(max = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    @Size(max = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank
    @Size(max = 255)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User toUser(User user) {
        user.setName(getName());
        user.setPassword(getPassword());
        user.setEmail(getEmail());

        return user;
    }
}
