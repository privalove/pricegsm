package com.pricegsm.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.pricegsm.domain.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WorkConditionsForm
        implements Serializable {

    private boolean sellerPickup;

    private boolean sellerDelivery;

    private String sellerDeliveryPlace;

    private String sellerPickupPlace;

    private Date sellerDeliveryFrom;

    private Date sellerDeliveryTo;

    private Date sellerPickupFrom;

    private Date sellerPickupTo;

    private boolean sellerDeliveryFree;

    private int sellerDeliveryMin;

    private boolean sellerDeliveryPaid;

    private BigDecimal sellerDeliveryCost;

    private int sellerFreeReplacement;

    private int sellerFreeRepair;

    private String sellerWarrantyAdditional;

    private String sellerPickupPlaceAddition;

    public WorkConditionsForm() {
    }

    public WorkConditionsForm(User user) {
        setSellerDelivery(user.isSellerDelivery());
        setSellerDeliveryCost(user.getSellerDeliveryCost());
        setSellerDeliveryFree(user.isSellerDeliveryFree());
        setSellerDeliveryFrom(user.getSellerDeliveryFrom());
        setSellerDeliveryTo(user.getSellerDeliveryTo());
        setSellerDeliveryMin(user.getSellerDeliveryMin());
        setSellerDeliveryPaid(user.isSellerDeliveryPaid());
        setSellerDeliveryPlace(user.getSellerDeliveryPlace());
        setSellerFreeRepair(user.getSellerFreeRepair());
        setSellerFreeReplacement(user.getSellerFreeReplacement());
        setSellerPickup(user.isSellerPickup());
        setSellerPickupFrom(user.getSellerPickupFrom());
        setSellerPickupTo(user.getSellerPickupTo());
        setSellerPickupPlace(user.getSellerPickupPlace());
        setSellerWarrantyAdditional(user.getSellerWarrantyAdditional());
        setSellerPickupPlaceAddition(user.getSellerPickupPlaceAddition());
    }

    public void fill(User user) {
        user.setSellerDelivery(isSellerDelivery());
        user.setSellerDeliveryCost(getSellerDeliveryCost());
        user.setSellerDeliveryFree(isSellerDeliveryFree());
        user.setSellerDeliveryFrom(getSellerDeliveryFrom());
        user.setSellerDeliveryTo(getSellerDeliveryTo());
        user.setSellerDeliveryMin(getSellerDeliveryMin());
        user.setSellerDeliveryPaid(isSellerDeliveryPaid());
        user.setSellerDeliveryPlace(getSellerDeliveryPlace());
        user.setSellerFreeRepair(getSellerFreeRepair());
        user.setSellerFreeReplacement(getSellerFreeReplacement());
        user.setSellerPickup(isSellerPickup());
        user.setSellerPickupFrom(getSellerPickupFrom());
        user.setSellerPickupTo(getSellerPickupTo());
        user.setSellerPickupPlace(getSellerPickupPlace());
        user.setSellerWarrantyAdditional(getSellerWarrantyAdditional());
        user.setSellerPickupPlaceAddition(getSellerPickupPlaceAddition());
    }

    public boolean isSellerPickup() {
        return sellerPickup;
    }

    public void setSellerPickup(boolean sellerPickup) {
        this.sellerPickup = sellerPickup;
    }

    public boolean isSellerDelivery() {
        return sellerDelivery;
    }

    public void setSellerDelivery(boolean sellerDelivery) {
        this.sellerDelivery = sellerDelivery;
    }

    public String getSellerDeliveryPlace() {
        return sellerDeliveryPlace;
    }

    public void setSellerDeliveryPlace(String sellerDeliveryPlace) {
        this.sellerDeliveryPlace = sellerDeliveryPlace;
    }

    public String getSellerPickupPlace() {
        return sellerPickupPlace;
    }

    public void setSellerPickupPlace(String sellerPickupPlace) {
        this.sellerPickupPlace = sellerPickupPlace;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getSellerDeliveryFrom() {
        return sellerDeliveryFrom;
    }

    public void setSellerDeliveryFrom(Date sellerDeliveryFrom) {
        this.sellerDeliveryFrom = sellerDeliveryFrom;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getSellerDeliveryTo() {
        return sellerDeliveryTo;
    }

    public void setSellerDeliveryTo(Date sellerDeliveryTo) {
        this.sellerDeliveryTo = sellerDeliveryTo;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getSellerPickupFrom() {
        return sellerPickupFrom;
    }

    public void setSellerPickupFrom(Date sellerPickupFrom) {
        this.sellerPickupFrom = sellerPickupFrom;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getSellerPickupTo() {
        return sellerPickupTo;
    }

    public void setSellerPickupTo(Date sellerPickupTo) {
        this.sellerPickupTo = sellerPickupTo;
    }

    public boolean isSellerDeliveryFree() {
        return sellerDeliveryFree;
    }

    public void setSellerDeliveryFree(boolean sellerDeliveryFree) {
        this.sellerDeliveryFree = sellerDeliveryFree;
    }

    public int getSellerDeliveryMin() {
        return sellerDeliveryMin;
    }

    public void setSellerDeliveryMin(int sellerDeliveryMin) {
        this.sellerDeliveryMin = sellerDeliveryMin;
    }

    public boolean isSellerDeliveryPaid() {
        return sellerDeliveryPaid;
    }

    public void setSellerDeliveryPaid(boolean sellerDeliveryPaid) {
        this.sellerDeliveryPaid = sellerDeliveryPaid;
    }

    public BigDecimal getSellerDeliveryCost() {
        return sellerDeliveryCost;
    }

    public void setSellerDeliveryCost(BigDecimal sellerDeliveryCost) {
        this.sellerDeliveryCost = sellerDeliveryCost;
    }

    public int getSellerFreeReplacement() {
        return sellerFreeReplacement;
    }

    public void setSellerFreeReplacement(int sellerFreeReplacement) {
        this.sellerFreeReplacement = sellerFreeReplacement;
    }

    public int getSellerFreeRepair() {
        return sellerFreeRepair;
    }

    public void setSellerFreeRepair(int sellerFreeRepair) {
        this.sellerFreeRepair = sellerFreeRepair;
    }

    public String getSellerWarrantyAdditional() {
        return sellerWarrantyAdditional;
    }

    public void setSellerWarrantyAdditional(String sellerWarrantyAdditional) {
        this.sellerWarrantyAdditional = sellerWarrantyAdditional;
    }

    public String getSellerPickupPlaceAddition() {
        return sellerPickupPlaceAddition;
    }

    public void setSellerPickupPlaceAddition(String sellerPickupPlaceAddition) {
        this.sellerPickupPlaceAddition = sellerPickupPlaceAddition;
    }
}
