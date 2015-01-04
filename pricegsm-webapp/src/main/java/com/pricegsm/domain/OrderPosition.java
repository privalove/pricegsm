package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_position")
public class OrderPosition
        extends GlobalEntity {

    private Order order;

    private Product product;

    private Specification specification;

    private BigDecimal totalPrice;

    private BigDecimal price;

    private int amount;

    private int version;

    private long priceListPosition;

    private String description;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "PriceList")
    @SequenceGenerator(name = "PriceList", sequenceName = "pricelist_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @ManyToOne()
    @JoinColumn(name = "specification_id", referencedColumnName = "id")
    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    @NotNull
    @Min(0)
    @Basic
    @Column(name = "total_price", nullable = false)
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Price per item
     */
    @NotNull
    @Min(0)
    @Basic
    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    @Min(0)
    @Basic
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Basic
    @Column(name = "price_list_position")
    public long getPriceListPosition() {
        return priceListPosition;
    }

    public void setPriceListPosition(long priceListPosition) {
        this.priceListPosition = priceListPosition;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!super.equals(o)) {
            return false;
        }

        // its only for new entities
        if (id == 0) {
            OrderPosition that = (OrderPosition) o;

            if (amount != that.amount) {
                return false;
            }
            if (priceListPosition != that.priceListPosition) {
                return false;
            }
            if (version != that.version) {
                return false;
            }
            if (description != null
                    ? !description.equals(that.description) : that.description != null) {
                return false;
            }
            if (order != null ? !order.equals(that.order) : that.order != null) {
                return false;
            }
            if (price != null ? !price.equals(that.price) : that.price != null) {
                return false;
            }
            if (product != null ? !product.equals(that.product) : that.product != null) {
                return false;
            }
            if (specification != null
                    ? !specification.equals(that.specification) : that.specification != null) {
                return false;
            }
            if (totalPrice != null ? !totalPrice.equals(that.totalPrice) : that.totalPrice != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        if (id == 0) {
            result = 31 * result + (order != null ? order.hashCode() : 0);
            result = 31 * result + (product != null ? product.hashCode() : 0);
            result = 31 * result + (specification != null ? specification.hashCode() : 0);
            result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
            result = 31 * result + (price != null ? price.hashCode() : 0);
            result = 31 * result + amount;
            result = 31 * result + version;
            result = 31 * result + (int) (priceListPosition ^ (priceListPosition >>> 32));
            result = 31 * result + (description != null ? description.hashCode() : 0);
        }
        return result;
    }
}
