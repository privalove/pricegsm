package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Price from market yandex.
 */
@Entity
@Table(name = "yandex_price")
public class YandexPrice
        extends GlobalEntity {

    private Product product;

    private BigDecimal priceRub = BigDecimal.ZERO;

    private BigDecimal priceEur = BigDecimal.ZERO;

    private BigDecimal priceUsd = BigDecimal.ZERO;

    private Date date;

    private int position;

    private String shop;

    private String link;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "YandexPrice")
    @SequenceGenerator(name = "YandexPrice", sequenceName = "yandex_price_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @NotNull
    @Basic
    @Column(name = "price_rub", nullable = false)
    public BigDecimal getPriceRub() {
        return priceRub;
    }

    public void setPriceRub(BigDecimal priceRub) {
        this.priceRub = priceRub;
    }

    @Basic
    @Column(name = "price_eur")
    public BigDecimal getPriceEur() {
        return priceEur;
    }

    public void setPriceEur(BigDecimal priceEur) {
        this.priceEur = priceEur;
    }

    @Basic
    @Column(name = "price_usd")
    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sell_date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "position")
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Shop name.
     */
    @Basic
    @Column(name = "shop")
    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    /**
     * Link to internet shop.
     */
    @Basic
    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
