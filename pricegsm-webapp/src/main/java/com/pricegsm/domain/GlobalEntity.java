package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Base class for entities which have simple ID.
 */
@MappedSuperclass
public class GlobalEntity
        extends BaseEntity {

    protected long id;

    public GlobalEntity() {
    }

    public GlobalEntity(long id) {
        this.id = id;
    }

    /**
     * Simple Primary Key
     */
    @Transient
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlobalEntity that = (GlobalEntity) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getId(), that.getId());

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }
}
