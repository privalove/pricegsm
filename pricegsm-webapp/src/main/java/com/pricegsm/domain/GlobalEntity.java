package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for entities which have simple ID.
 */
@MappedSuperclass
public class GlobalEntity
        extends BaseEntity {

    protected long id;

    /**
     * Simple Primary Key
     */
    @Id
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
