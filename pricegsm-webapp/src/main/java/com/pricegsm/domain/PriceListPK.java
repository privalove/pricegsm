package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class PriceListPK
        implements Serializable {

    private int position;

    private long user;

    public PriceListPK() {
    }

    public PriceListPK(long user, int position) {
        this.user = user;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceListPK that = (PriceListPK) o;
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
