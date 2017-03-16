package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * User: o.logunov
 * Date: 19.02.14
 * Time: 23:19
 */
public class ProductForm {

    private String yandexId;

    private String name;

    private String searchQuery;

    private String searchPriceListQuery;

    private String dunamisQuery;

    private String excludeQuery;

    private ProductType type;

    private Vendor vendor;

    private boolean active = true;

    private String description;

    private List<ColorProductForm> colors = new ArrayList<>();

    private boolean manufacturerWarranty = false;

    public ProductForm() {
    }

    public ProductForm(List<Product> products) {
        List<ColorProductForm> colors = new ArrayList<>();
        for (Product product : products) {
            colors.add(
                    new ColorProductForm(
                            product.getId(), product.getColor(), product.getColorQuery(), product.getExcludedColorQuery())
            );
        }

        Product product = products.get(0);
        setYandexId(product.getYandexId());
        setName(product.getName());
        setSearchQuery(product.getSearchQuery());
        setSearchPriceListQuery(product.getSearchPriceListQuery());
        setDunamisQuery(product.getDunamisQuery());
        setExcludeQuery(product.getExcludeQuery());
        setType(product.getType());
        setVendor(product.getVendor());
        setActive(product.isActive());
        setDescription(product.getDescription());
        setColors(colors);
        setManufacturerWarranty(product.isManufacturerWarranty());
    }

    public ProductForm(
            String yandexId, String name,
            String searchQuery, String searchPriceListQuery, String excludeQuery,
            ProductType type, Vendor vendor, boolean active,
            String description, List<ColorProductForm> colors, boolean manufacturerWarranty) {
        this.name = name;
        this.yandexId = yandexId;
        this.searchQuery = searchQuery;
        this.searchPriceListQuery = searchPriceListQuery;
        this.excludeQuery = excludeQuery;
        this.type = type;
        this.vendor = vendor;
        this.active = active;
        this.description = description;
        this.colors = colors;
        this.manufacturerWarranty = manufacturerWarranty;
    }

    public List<Product> convertToProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (ColorProductForm color : colors) {
            products.add(
                    new Product(
                            color.getProductId(), getName(), getYandexId(), getSearchQuery(),
                            getSearchPriceListQuery(), getDunamisQuery(), getExcludeQuery(), color.getColorQuery(),
                            color.getExcludedColorQuery(), getType(), getVendor(), color.getColor(), isActive(),
                            getDescription(), isManufacturerWarranty())
            );
        }
        return products;
    }

    // Getters and setters
    @NotBlank
    @Size(max = 255)
    public String getName() {
        return name;
    }

    @NotBlank
    @Size(max = 255)
    public String getYandexId() {
        return yandexId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public String getExcludeQuery() {
        return excludeQuery;
    }

    @NotNull
    public ProductType getType() {
        return type;
    }

    @NotNull
    public Vendor getVendor() {
        return vendor;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    @Valid
    public List<ColorProductForm> getColors() {
        return colors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYandexId(String yandexId) {
        this.yandexId = yandexId;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setExcludeQuery(String excludeQuery) {
        this.excludeQuery = excludeQuery;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColors(List<ColorProductForm> colors) {
        this.colors = colors;
    }

    public String getSearchPriceListQuery() {
        return searchPriceListQuery;
    }

    public void setSearchPriceListQuery(String searchPriceListQuery) {
        this.searchPriceListQuery = searchPriceListQuery;
    }

    public String getDunamisQuery() {
        return dunamisQuery;
    }

    public void setDunamisQuery(String dunamisQuery) {
        this.dunamisQuery = dunamisQuery;
    }

    public boolean isManufacturerWarranty() {
        return manufacturerWarranty;
    }

    public void setManufacturerWarranty(boolean manufacturerWarranty) {
        this.manufacturerWarranty = manufacturerWarranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductForm that = (ProductForm) o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(isActive(), that.isActive());
        builder.append(getColors(), that.getColors());
        builder.append(getDescription(), that.getDescription());
        builder.append(getExcludeQuery(), that.getExcludeQuery());
        builder.append(getName(), that.getName());
        builder.append(getSearchQuery(), that.getSearchQuery());
        builder.append(getSearchPriceListQuery(), that.getSearchPriceListQuery());
        builder.append(getType(), that.getType());
        builder.append(getVendor(), that.getVendor());
        builder.append(getYandexId(), that.getYandexId());
        builder.append(isManufacturerWarranty(), that.isManufacturerWarranty());

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getName())
                .append(getYandexId())
                .append(getSearchQuery())
                .append(getSearchPriceListQuery())
                .append(getExcludeQuery())
                .append(getType())
                .append(getVendor())
                .append(isActive())
                .append(getDescription())
                .append(getColors())
                .append(isManufacturerWarranty())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("yandexId", yandexId)
                .append("name", name)
                .append("searchQuery", searchQuery)
                .append("searchPriceListQuery", searchPriceListQuery)
                .append("excludeQuery", excludeQuery)
                .append("type", type)
                .append("vendor", vendor)
                .append("active", active)
                .append("description", description)
                .append("colors", colors)
                .append("manufacturerWarranty", manufacturerWarranty)
                .toString();
    }
}
