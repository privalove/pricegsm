package com.pricegsm.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.pricegsm.domain.Region;
import com.pricegsm.domain.User;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class ProfileForm implements Serializable {

    private String name;

    private String organizationName;

    private String phone;

    private String website;

    private String buyerDeliveryPlace;

    private Date buyerDeliveryFrom;

    private Date buyerDeliveryTo;

    private Region region;

    public ProfileForm() {
    }

    public ProfileForm(User user) {
        setName(user.getName());
        setOrganizationName(user.getOrganizationName());
        setPhone(user.getPhone());
        setWebsite(user.getWebsite());
        setBuyerDeliveryPlace(user.getBuyerDeliveryPlace());
        setBuyerDeliveryFrom(user.getBuyerDeliveryFrom());
        setBuyerDeliveryTo(user.getBuyerDeliveryTo());
        setRegion(user.getRegion());
    }

    public void fill(User user) {
        user.setName(getName());
        user.setOrganizationName(getOrganizationName());
        user.setPhone(getPhone());
        user.setWebsite(getWebsite());
        user.setBuyerDeliveryPlace(getBuyerDeliveryPlace());
        user.setBuyerDeliveryFrom(getBuyerDeliveryFrom());
        user.setBuyerDeliveryTo(getBuyerDeliveryTo());
        user.setRegion(getRegion());

    }

    /**
     * Name of the user which is visible for his partners.
     */
    @NotBlank
    @Size(max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(max = 255)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Size(max = 255)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Size(max = 255)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Default place for delivery.
     */
    @Size(max = 255)
    public String getBuyerDeliveryPlace() {
        return buyerDeliveryPlace;
    }

    public void setBuyerDeliveryPlace(String buyerDeliveryPlace) {
        this.buyerDeliveryPlace = buyerDeliveryPlace;
    }

    /**
     * Default delivery time range from.
     */
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    public Date getBuyerDeliveryFrom() {
        return buyerDeliveryFrom;
    }

    public void setBuyerDeliveryFrom(Date buyer_delivery_from) {
        this.buyerDeliveryFrom = buyer_delivery_from;
    }

    /**
     * Default delivery time range to.
     */
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    public Date getBuyerDeliveryTo() {
        return buyerDeliveryTo;
    }

    public void setBuyerDeliveryTo(Date buyer_delivery_to) {
        this.buyerDeliveryTo = buyer_delivery_to;
    }

    /**
     * Current region of user.
     */
    @NotNull
    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
