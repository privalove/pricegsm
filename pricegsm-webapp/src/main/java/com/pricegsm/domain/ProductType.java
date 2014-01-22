package com.pricegsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product_type")
public class ProductType
        extends GlobalEntity
        implements Activable {

    private String name;

    private long yandexId;

    private boolean active = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "ProductType")
    @SequenceGenerator(name = "ProductType", sequenceName = "product_type_seq", allocationSize = 1)
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

    @JsonIgnore
    @Basic
    @Column(name = "yandex_id", nullable = false)
    public long getYandexId() {
        return yandexId;
    }

    public void setYandexId(long yandexId) {
        this.yandexId = yandexId;
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
