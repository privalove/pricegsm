package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "partner")
public class Partner
        extends GlobalEntity {

    private User user;

    private Partner partner;

    private boolean approved;

    private boolean confirmed;

    private boolean showPriceList = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Partner")
    @SequenceGenerator(name = "Partner", sequenceName = "partner_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

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

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id", referencedColumnName = "id", nullable = false)
    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * Approved by partner.
     */
    @Basic
    @Column(name = "approved", nullable = false)
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Approved by user.
     */
    @Basic
    @Column(name = "confirmed", nullable = false)
    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Basic
    @Column(name = "show_pricelist", nullable = false)
    public boolean isShowPriceList() {
        return showPriceList;
    }

    public void setShowPriceList(boolean showPriceList) {
        this.showPriceList = showPriceList;
    }
}
