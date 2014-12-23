package com.pricegsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;

/**
 * User: o.logunov
 * Date: 11.12.14
 * Time: 23:40
 */
@Entity
@Table(name = "marketplace_filter")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class MarketplaceFilter extends GlobalEntity {

    private User user;

    private Vendor vendor;

    private Product product;

    public MarketplaceFilter() {
    }

    public MarketplaceFilter(User user, Vendor vendor, Product product) {
        this.user = user;
        this.vendor = vendor;
        this.product = product;
    }

    @Id
    @GeneratedValue(generator = "MarketplaceFilter")
    @SequenceGenerator(name = "MarketplaceFilter", sequenceName = "marketplace_filter_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    public long getId() {
        return super.getId();
    }

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
