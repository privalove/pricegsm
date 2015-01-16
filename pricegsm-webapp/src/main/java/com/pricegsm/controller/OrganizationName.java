package com.pricegsm.controller;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: o.logunov
 * Date: 16.01.15
 * Time: 23:12
 */
public class OrganizationName implements Serializable {

    private String organizationName;

    public OrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public OrganizationName() {
    }

    @Size(max = 255)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
