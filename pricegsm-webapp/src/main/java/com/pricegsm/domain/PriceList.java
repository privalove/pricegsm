package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pricelist")
@IdClass(PriceListPK.class)
public class PriceList
        extends BaseEntity
        implements Activable {

    List<PriceListPosition> positions = new ArrayList<>();

    private int version;

    private boolean active = true;

    private Date sellFromDate;

    private Date sellToDate;

    private User user;

    private Currency currency;

    private String description;

    private int position;

    @Id
    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "position", nullable = false)
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Valid
    @OneToMany(mappedBy = "priceList", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PriceListPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PriceListPosition> positions) {
        this.positions = positions;
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

    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceList that = (PriceList) o;
        return new EqualsBuilder()
                .append(getPosition(), that.getPosition())
                .append(getUser(), that.getUser())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getPosition())
                .append(getUser())
                .toHashCode();
    }
}
