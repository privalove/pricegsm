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
 * Price from world trades.
 */
@Entity
@Table(name = "world_price")
public class WorldPrice
        extends GlobalEntity {

    private Product product;

    private BigDecimal priceRub = BigDecimal.ZERO;

    private BigDecimal priceEur = BigDecimal.ZERO;

    private BigDecimal priceUsd = BigDecimal.ZERO;

    private String description;

    private Date date;

    private int position;

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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Basic
    @Column(name = "price_rub")
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

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
