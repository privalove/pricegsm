package com.pricegsm.domain;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "order")
public class Order
        extends GlobalEntity {

    public static enum Status {
        PREPARE, SENT, CANCELED, CONFIRMED, DECLINED
    }

    private Set<OrderPosition> orderPositions = new HashSet<>();

    private User buyer;

    private User seller;

    private int priceListPosition;

    private Date sendDate;

    private Date creationDate;

    private Status status = Status.PREPARE;

    private int version;

    private String phone;

    private String contactName;

    private boolean delivery;

    private boolean pickup;

    private boolean deliveryFree;

    private BigDecimal deliveryCost = BigDecimal.ZERO;

    private String place;

    private Date fromTime;

    private Date toTime;

    private Date deliveryDate;

    private String description;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    private int totalAmount;

    private Currency currency;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Order")
    @SequenceGenerator(name = "Order", sequenceName = "order_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @Valid
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn(name = "position")
    public Set<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(Set<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }

    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", nullable = false)
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @Column(name = "price_list_position", nullable = false)
    public int getPriceListPosition() {
        return priceListPosition;
    }

    public void setPriceListPosition(int position) {
        this.priceListPosition = position;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "send_date")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date send_date) {
        this.sendDate = send_date;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @NotNull
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NotBlank
    @Size(max = 255)
    @Basic
    @Column(name = "phone", nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NotBlank
    @Size(max = 255)
    @Basic
    @Column(name = "contact_name", nullable = false)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Basic
    @Column(name = "delivery", nullable = false)
    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    @Basic
    @Column(name = "pickup", nullable = false)
    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    @Basic
    @Column(name = "free_delivery", nullable = false)
    public boolean isDeliveryFree() {
        return deliveryFree;
    }

    public void setDeliveryFree(boolean deliveryFree) {
        this.deliveryFree = deliveryFree;
    }

    @Min(0)
    @Basic
    @Column(name = "delivery_cost")
    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "place")
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @JsonSerialize(using = DateSerializer.class)
    @Basic
    @Temporal(TemporalType.TIME)
    @Column(name = "from_time")
    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    @JsonSerialize(using = DateSerializer.class)
    @Basic
    @Temporal(TemporalType.TIME)
    @Column(name = "to_time")
    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date date) {
        this.deliveryDate = date;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @NotNull
    @Min(0)
    @Basic
    @Column(name = "total_amount", nullable = false)
    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
