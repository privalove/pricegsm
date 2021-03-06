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

    private String searchQuery;

    private String searchPriceListQuery;

    private String dunamisQuery;

    private String excludeQuery;

    private String colorQuery;

    private String excludedColorQuery;

    private ProductType type;

    private Vendor vendor;

    private Color color;

    private boolean active = true;

    private String description;

    private boolean manufacturerWarranty = false;

    public Product() {
    }

    public Product(
            long id, String name, String yandexId, String searchQuery,
            String searchPriceListQuery, String dunamisQuery, String excludeQuery,
            String colorQuery, String excludedColorQuery, ProductType type, Vendor vendor, Color color,
            boolean active, String description, boolean manufacturerWarranty) {

        super(id);
        this.name = name;
        this.yandexId = yandexId;
        this.searchQuery = searchQuery;
        this.searchPriceListQuery = searchPriceListQuery;
        this.dunamisQuery = dunamisQuery;
        this.excludeQuery = excludeQuery;
        this.colorQuery = colorQuery;
        this.type = type;
        this.vendor = vendor;
        this.color = color;
        this.active = active;
        this.description = description;
        this.excludedColorQuery = excludedColorQuery;
        this.manufacturerWarranty = manufacturerWarranty;
    }

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
    @Column(name = "yandex_id")
    public String getYandexId() {
        return yandexId;
    }

    public void setYandexId(String yandex_id) {
        this.yandexId = yandex_id;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "search_query")
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "search_pl_query")
    public String getSearchPriceListQuery() {
        return searchPriceListQuery;
    }

    public void setSearchPriceListQuery(String searchPriceListQuery) {
        this.searchPriceListQuery = searchPriceListQuery;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "dunamis_product_name")
    public String getDunamisQuery() {
        return dunamisQuery;
    }

    public void setDunamisQuery(String dunamisQuery) {
        this.dunamisQuery = dunamisQuery;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "exclude_query")
    public String getExcludeQuery() {
        return excludeQuery;
    }

    public void setExcludeQuery(String excludeQuery) {
        this.excludeQuery = excludeQuery;
    }

    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_type_id", referencedColumnName = "id", nullable = false)
    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "colorQuery")
    public String getColorQuery() {
        return colorQuery;
    }

    public void setColorQuery(String colorQuery) {
        this.colorQuery = colorQuery;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "excluded_color_query")
    public String getExcludedColorQuery() {
        return excludedColorQuery;
    }

    public void setExcludedColorQuery(String excludedColorQuery) {
        this.excludedColorQuery = excludedColorQuery;
    }

    @Basic
    @Column(name = "manufacturer_warranty", nullable = false)
    public boolean isManufacturerWarranty() {
        return manufacturerWarranty;
    }

    public void setManufacturerWarranty(boolean manufacturerWarranty) {
        this.manufacturerWarranty = manufacturerWarranty;
    }
}
