package com.pricegsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * User: o.logunov
 * Date: 28.09.14
 * Time: 21:17
 */
@Entity
@Table(name = "prices")
public class Price extends GlobalEntity {

    private PriceListPosition priceListPosition;

    private BigDecimal price = BigDecimal.ZERO;

    private int minOrderQuantity;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Price")
    @SequenceGenerator(name = "Price", sequenceName = "prices_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "price_list_position_id", referencedColumnName = "id", nullable = false)
    public PriceListPosition getPriceListPosition() {
        return priceListPosition;
    }

    public void setPriceListPosition(PriceListPosition priceListPosition) {
        this.priceListPosition = priceListPosition;
    }

    @Min(0)
    @NotNull
    @Basic
    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Min(0)
    @NotNull
    @Basic
    @Column(name = "min_order_quantity", nullable = false)
    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(int amount) {
        this.minOrderQuantity = amount;
    }
}
