package com.pricegsm.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "pricelist")
public class PriceList
        extends GlobalEntity
        implements Activable {

    private int version;

    private boolean active = true;

    private Date sellFromDate;

    private Date sellToDate;

    private BigDecimal price = BigDecimal.ZERO;

    private Product product;

    private User user;

    private Currency currency;

    private int amount = 1;

    private int moq = 1;

    private String description;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "PriceList")
    @SequenceGenerator(name = "PriceList", sequenceName = "pricelist_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @Basic
    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NotNull
    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "sell_from_date", nullable = false)
    public Date getSellFromDate() {
        return sellFromDate;
    }

    public void setSellFromDate(Date sellFromDate) {
        this.sellFromDate = sellFromDate;
    }

    @NotNull
    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "sell_to_date", nullable = false)
    public Date getSellToDate() {
        return sellToDate;
    }

    public void setSellToDate(Date sellToDate) {
        this.sellToDate = sellToDate;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Min(1)
    @NotNull
    @Basic
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Minimal Order Quantity
     */
    @Min(1)
    @NotNull
    @Basic
    @Column(name = "min_order_quantity", nullable = false)
    public int getMoq() {
        return moq;
    }

    public void setMoq(int moq) {
        this.moq = moq;
    }

    @Lob
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Version for optimistic lock
     */
    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
