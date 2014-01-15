package com.pricegsm.controller.admin;

import com.pricegsm.domain.Administrator;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class ProfileForm implements Serializable {

    private String name;

    public ProfileForm() {
    }

    public ProfileForm(Administrator user) {
        setName(user.getName());
    }

    public void fill(Administrator administrator) {
        administrator.setName(getName());
    }

    @NotBlank
    @Size(max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
