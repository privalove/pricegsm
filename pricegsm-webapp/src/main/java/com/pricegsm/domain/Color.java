package com.pricegsm.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Color of product.
 */
@Entity
@Table(name = "color")
public class Color
        extends GlobalEntity
        implements Activable {

    private String name;

    private String description;

    private String yandexColor;

    private String code;

    private boolean active = true;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Color")
    @SequenceGenerator(name = "Color", sequenceName = "color_seq", allocationSize = 1)
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

    /**
     * Color name for yandex parser
     */
    @Size(max = 255)
    @Basic
    @Column(name = "yandex_color")
    public String getYandexColor() {
        return yandexColor;
    }

    public void setYandexColor(String yandexColor) {
        this.yandexColor = yandexColor;
    }


    /**
     * Rgb code #FFFFFF
     */
    @Size(max = 255)
    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
