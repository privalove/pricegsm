package com.pricegsm.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.pricegsm.domain.Partner;
import com.pricegsm.domain.User;

import java.io.Serializable;
import java.util.Date;

/**
 * User: o.logunov
 * Date: 17.01.15
 * Time: 17:14
 */
public class PartnerUIModel implements Serializable {

    private long id;

    private long partnerId;

    private String partnerOrganizationName;

    private String partnerName;

    private String partnerPhone;

    private String partnerWebsite;

    private String partnerCity;

    private boolean approved;

    private boolean confirmed;

    private boolean showPriceList;

    private Date lastOrderDate;

    private Date lastPriceList;

    public PartnerUIModel() {
    }

    public PartnerUIModel(Partner partner, Date lastOrderDate, Date lastPriceList) {

        this.id = partner.getId();
        this.lastOrderDate = lastOrderDate;
        this.lastPriceList = lastPriceList;
        this.approved = partner.isApproved();
        this.confirmed = partner.isConfirmed();
        this.showPriceList = partner.isShowPriceList();

        User partnerUser = partner.getPartner();
        this.partnerId = partnerUser.getId();

        this.partnerName = partnerUser.getName();

        this.partnerOrganizationName = partnerUser.getOrganizationName();

        this.partnerPhone = partnerUser.getPhone();

        this.partnerWebsite = partnerUser.getWebsite();

        this.partnerCity = partnerUser.getRegion().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(Date lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getLastPriceList() {
        return lastPriceList;
    }

    public void setLastPriceList(Date lastPriceList) {
        this.lastPriceList = lastPriceList;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerOrganizationName() {
        return partnerOrganizationName;
    }

    public void setPartnerOrganizationName(String partnerOrganizationName) {
        this.partnerOrganizationName = partnerOrganizationName;
    }

    public String getPartnerPhone() {
        return partnerPhone;
    }

    public void setPartnerPhone(String partnerPhone) {
        this.partnerPhone = partnerPhone;
    }

    public String getPartnerWebsite() {
        return partnerWebsite;
    }

    public void setPartnerWebsite(String partnerWebsite) {
        this.partnerWebsite = partnerWebsite;
    }

    public String getPartnerCity() {
        return partnerCity;
    }

    public void setPartnerCity(String partnerCity) {
        this.partnerCity = partnerCity;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isShowPriceList() {
        return showPriceList;
    }

    public void setShowPriceList(boolean showPriceList) {
        this.showPriceList = showPriceList;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
