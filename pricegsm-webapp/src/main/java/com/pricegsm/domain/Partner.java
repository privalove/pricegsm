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

    private User partner;

    private boolean approved;

    private boolean confirmed;

    private boolean showPriceList = true;

    public Partner() {
    }

    public Partner(User user, User partner, boolean approved, boolean confirmed) {
        this.user = user;
        this.partner = partner;
        this.approved = approved;
        this.confirmed = confirmed;
    }

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
    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partner)) return false;
        if (!super.equals(o)) return false;

        Partner partner1 = (Partner) o;
        if (id == 0) {
            if (approved != partner1.approved) return false;
            if (confirmed != partner1.confirmed) return false;
            if (showPriceList != partner1.showPriceList) return false;
            if (partner != null ? !partner.equals(partner1.partner) : partner1.partner != null) return false;
            //noinspection RedundantIfStatement
            if (user != null ? !user.equals(partner1.user) : partner1.user != null) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        if (id == 0) {
            result = 31 * result + (user != null ? user.hashCode() : 0);
            result = 31 * result + (partner != null ? partner.hashCode() : 0);
            result = 31 * result + (approved ? 1 : 0);
            result = 31 * result + (confirmed ? 1 : 0);
            result = 31 * result + (showPriceList ? 1 : 0);
        }
        return result;
    }
}
