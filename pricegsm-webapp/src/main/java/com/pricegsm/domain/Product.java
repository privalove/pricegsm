package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product
        extends GlobalEntity
        implements Activable {

    private String name;

    private String yandexId;

    private Vendor vendor;

    private Color color;

    private boolean active = true;

    private String description;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Product")
    @SequenceGenerator(name = "Product", sequenceName = "product_seq", allocationSize = 1)
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

    @NotBlank
    @Size(max = 255)
    @Basic
    @Column(name = "yandex_id", nullable = false)
    public String getYandexId() {
        return yandexId;
    }

    public void setYandexId(String yandex_id) {
        this.yandexId = yandex_id;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "color_id", referencedColumnName = "id", nullable = false)
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Basic
    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Lob
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
