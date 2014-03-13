package com.pricegsm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * User: o.logunov
 * Date: 21.02.14
 * Time: 23:03
 */
public class ColorProductForm {

    private Long productId;

    private Color color;

    private String colorQuery;

    public ColorProductForm() {
    }

    public ColorProductForm(Long productId, Color color, String colorQuery) {
        this.productId = productId;
        this.color = color;
        this.colorQuery = colorQuery;
    }

    public Long getProductId() {
        return productId;
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    public String getColorQuery() {
        return colorQuery;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColorQuery(String colorQuery) {
        this.colorQuery = colorQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorProductForm that = (ColorProductForm) o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getProductId(), that.getProductId());
        builder.append(getColor(), that.getColor());
        builder.append(getColorQuery(), that.getColorQuery());

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getProductId())
                .append(getColor())
                .append(getColorQuery())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productId", productId)
                .append("color", color)
                .append("colorQuery", colorQuery)
                .toString();
    }
}
