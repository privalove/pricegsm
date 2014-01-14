package com.pricegsm.domain;

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

    @Lob
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
}
