package com.pricegsm.controller;

import com.pricegsm.domain.Product;
import com.pricegsm.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductPriceForm
        implements Serializable {

    private long id;

    private String product;

    private String color;

    private String vendor;

    private BigDecimal retail = BigDecimal.ZERO;

    private BigDecimal retailDelta = BigDecimal.ZERO;

    private BigDecimal opt = BigDecimal.ZERO;

    private BigDecimal optDelta = BigDecimal.ZERO;

    private BigDecimal world = BigDecimal.ZERO;

    private BigDecimal worldDelta = BigDecimal.ZERO;

    public ProductPriceForm() {
    }

    public ProductPriceForm(Product product, String previousProduct, BigDecimal retail, BigDecimal retailDelta, BigDecimal opt, BigDecimal optDelta, BigDecimal world, BigDecimal worldDelta) {
        this.product = Utils.isEmpty(previousProduct) || !previousProduct.equals(product.getName()) ? product.getName() : "";
        this.id = product.getId();
        this.vendor = product.getVendor().getName();
        this.color = product.getColor().getName();
        this.retail = retail;
        this.retailDelta = retailDelta;
        this.opt = opt;
        this.optDelta = optDelta;
        this.world = world;
        this.worldDelta = worldDelta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public BigDecimal getRetail() {
        return retail;
    }

    public void setRetail(BigDecimal retail) {
        this.retail = retail;
    }

    public BigDecimal getOpt() {
        return opt;
    }

    public void setOpt(BigDecimal opt) {
        this.opt = opt;
    }

    public BigDecimal getWorld() {
        return world;
    }

    public void setWorld(BigDecimal world) {
        this.world = world;
    }

    public BigDecimal getRetailDelta() {
        return retailDelta;
    }

    public void setRetailDelta(BigDecimal retailDelta) {
        this.retailDelta = retailDelta;
    }

    public BigDecimal getOptDelta() {
        return optDelta;
    }

    public void setOptDelta(BigDecimal optDelta) {
        this.optDelta = optDelta;
    }

    public BigDecimal getWorldDelta() {
        return worldDelta;
    }

    public void setWorldDelta(BigDecimal worldDelta) {
        this.worldDelta = worldDelta;
    }
}
