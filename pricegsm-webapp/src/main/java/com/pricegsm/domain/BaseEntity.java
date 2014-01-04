package com.pricegsm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for almost all entities.
 */
@MappedSuperclass
public class BaseEntity
        implements Serializable {

    protected long modifiedBy;

    protected Date modified;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        setModified(new Date());
    }

    /**
     * ID of user, who created/last modified this entity.
     */
    @Basic
    @Column(name = "modified_by")
    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Time of creation/last modification of the entity
     */
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
