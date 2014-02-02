package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Product Region USA/EURO/UK etc.
 */
@Entity
@Table(name = "specification")
public class Specification
        extends GlobalEntity
        implements Activable {

    public static final Long USA = 1L;

    private String name;

    private String description;

    private boolean active = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Specification")
    @SequenceGenerator(name = "Specification", sequenceName = "specification_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @NotBlank
    @Size(max = 255)
    @Basic
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        Specification that = (Specification) o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(isActive(), that.isActive())
                .append(getName(), that.getName())
                .append(getDescription(), that.getDescription());

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(super.hashCode())
                .append(getName())
                .append(isActive())
                .append(getDescription())
                .toHashCode();
    }
}
