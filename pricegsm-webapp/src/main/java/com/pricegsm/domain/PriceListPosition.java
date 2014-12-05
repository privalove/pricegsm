package com.pricegsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "pricelist_position")
public class PriceListPosition
        extends GlobalEntity
        implements Activable {

    private int version;

    private boolean active = true;

    private Set<Price> prices = new LinkedHashSet<>();

    private Product product;

    private Specification specification;

    private PriceList priceList;

    private int amount = 1;

    private String description;

    public PriceListPosition() {
    }

    public PriceListPosition(Product product, PriceList priceList) {
        this.product = product;
        this.priceList = priceList;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "PriceListPosition")
    @SequenceGenerator(name = "PriceListPosition", sequenceName = "pricelist_position_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "pricelist_user_id", referencedColumnName = "user_id", nullable = false),
            @JoinColumn(name = "pricelist_position", referencedColumnName = "position", nullable = false)
    })
    public PriceList getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }

    @Basic
    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Valid
    @OneToMany(mappedBy = "priceListPosition", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

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

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @ManyToOne()
    @JoinColumn(name = "specification_id", referencedColumnName = "id")
    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    @Min(0)
    @NotNull
    @Basic
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
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
