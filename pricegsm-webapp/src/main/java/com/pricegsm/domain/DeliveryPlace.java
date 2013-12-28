package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Market or District
 */
@Entity
@Table(name = "delivery_place")
public class DeliveryPlace
        extends GlobalEntity
        implements Activable {

    private Region region;

    private String name;

    private String description;

    private boolean active = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "DeliveryPlace")
    @SequenceGenerator(name = "DeliveryPlace", sequenceName = "delivery_place_seq", allocationSize = 1)
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

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
