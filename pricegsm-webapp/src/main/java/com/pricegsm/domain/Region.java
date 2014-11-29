package com.pricegsm.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Region trade.
 */
@Entity
@Table(name = "region")
public class Region
        extends GlobalEntity
        implements Activable {

    public static final Long MOSCOW = 1L;

    private Set<DeliveryPlace> deliveryPlaces;

    private String name;

    private String description;

    private boolean active = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Region")
    @SequenceGenerator(name = "Region", sequenceName = "region_seq", allocationSize = 1)
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

    @Valid
    @OneToMany(mappedBy = "region", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<DeliveryPlace> getDeliveryPlaces() {
        return deliveryPlaces;
    }

    public void setDeliveryPlaces(Set<DeliveryPlace> deliveryPlaces) {
        this.deliveryPlaces = deliveryPlaces;
    }
}
