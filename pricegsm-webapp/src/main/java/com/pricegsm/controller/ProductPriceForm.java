package com.pricegsm.controller;

import com.pricegsm.domain.Product;
import com.pricegsm.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductPriceForm
        implements Serializable {


    private Product product;

    private String name;

    private BigDecimal retail = BigDecimal.ZERO;

    private BigDecimal retailDelta = BigDecimal.ZERO;

    private BigDecimal opt = BigDecimal.ZERO;

    private BigDecimal optDelta = BigDecimal.ZERO;

    private BigDecimal world = BigDecimal.ZERO;

    private BigDecimal worldDelta = BigDecimal.ZERO;

    private int count;

    public ProductPriceForm() {
    }

    public ProductPriceForm(Product product, String previousProduct, BigDecimal retail,  BigDecimal retailDelta, int count, BigDecimal opt, BigDecimal optDelta, BigDecimal world, BigDecimal worldDelta) {
        this.name = Utils.isEmpty(previousProduct) || !previousProduct.equals(product.getName()) ? product.getName() : "";
        this.product = product;
        this.retail = retail;
        this.retailDelta = retailDelta;
        this.count = count;
        this.opt = opt;
        this.optDelta = optDelta;
        this.world = world;
        this.worldDelta = worldDelta;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
