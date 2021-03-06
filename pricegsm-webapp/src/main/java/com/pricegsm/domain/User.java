package com.pricegsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Customer of the application.
 */
@Entity
@Table(name = "pricegsm_user")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class User
        extends BaseUser {

    public static final Date TIME_10_00 = new Date(10 * 60 * 60 * 1000);

    public static final Date TIME_17_00 = new Date(17 * 60 * 60 * 1000);

    private String organizationName;

    private String phone;

    private String managerPhone;

    private String website;

    private boolean sellerPickup;

    private boolean sellerDelivery = true;

    private boolean buyerDelivery;

    private String sellerDeliveryPlace;

    private String sellerPickupPlace;

    private String sellerPickupPlaceAddition;

    private Date sellerDeliveryFrom = TIME_10_00;

    private Date sellerDeliveryTo = TIME_17_00;

    private Date sellerPickupFrom = TIME_10_00;

    private Date sellerPickupTo = TIME_17_00;

    private Date deadLine = TIME_17_00;

    private boolean sellerDeliveryFree;

    private int sellerDeliveryMin = 1;

    private boolean sellerDeliveryPaid = true;

    private BigDecimal sellerDeliveryCost = new BigDecimal(300);

    private int sellerFreeReplacement;

    private int sellerFreeRepair;

    private String sellerWarrantyAdditional;

    private String buyerDeliveryPlace;

    private Date buyerDeliveryFrom = TIME_10_00;

    private Date buyerDeliveryTo = TIME_17_00;

    private Region region;

    private BigDecimal balance = BigDecimal.ZERO;

    private String token;

    private boolean emailValid;

    private Set<MarketplaceFilter> marketplaceFilters;

    private Set<Partner> partners;

    public User() {
    }

    public User(String name, String email, String password) {
        super(name, email, password);
    }

    @Size(max = 255)
    @Basic
    @Column(name = "organization_name", unique = true)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "manager_phone")
    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "website")
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Is pickup possible.
     */
    @Basic
    @Column(name = "seller_pickup", nullable = false)
    public boolean isSellerPickup() {
        return sellerPickup;
    }

    public void setSellerPickup(boolean sellerPickup) {
        this.sellerPickup = sellerPickup;
    }

    /**
     * Is delivery possible.
     */
    @Basic
    @Column(name = "seller_delivery", nullable = false)
    public boolean isSellerDelivery() {
        return sellerDelivery;
    }

    public void setSellerDelivery(boolean sellerDelivery) {
        this.sellerDelivery = sellerDelivery;
    }

    /**
     * Is default delivery place present.
     */
    @Basic
    @Column(name = "buyer_delivery", nullable = false)
    public boolean isBuyerDelivery() {
        return buyerDelivery;
    }

    public void setBuyerDelivery(boolean buyerDelivery) {
        this.buyerDelivery = buyerDelivery;
    }

    /**
     * Delivery place: market, city, region.
     */
    @Size(max = 255)
    @Basic
    @Column(name = "seller_delivery_place")
    public String getSellerDeliveryPlace() {
        return sellerDeliveryPlace;
    }

    public void setSellerDeliveryPlace(String sellerDeliveryPlace) {
        this.sellerDeliveryPlace = sellerDeliveryPlace;
    }

    /**
     * Pickup place.
     */
    @Size(max = 255)
    @Basic
    @Column(name = "seller_pickup_place")
    public String getSellerPickupPlace() {
        return sellerPickupPlace;
    }

    public void setSellerPickupPlace(String sellerPickupPlace) {
        this.sellerPickupPlace = sellerPickupPlace;
    }

    /**
     * Delivery possible from time.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "seller_delivery_from")
    public Date getSellerDeliveryFrom() {
        return sellerDeliveryFrom;
    }

    public void setSellerDeliveryFrom(Date sellerDeliveryFrom) {
        this.sellerDeliveryFrom = sellerDeliveryFrom;
    }

    /**
     * Delivery possible to time.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "seller_delivery_to")
    public Date getSellerDeliveryTo() {
        return sellerDeliveryTo;
    }

    public void setSellerDeliveryTo(Date seller_delivery_to) {
        this.sellerDeliveryTo = seller_delivery_to;
    }

    /**
     * Pickup possible from time.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "seller_pickup_from")
    public Date getSellerPickupFrom() {
        return sellerPickupFrom;
    }

    public void setSellerPickupFrom(Date seller_pickup_from) {
        this.sellerPickupFrom = seller_pickup_from;
    }

    /**
     * Pickup possible to time.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "seller_pickup_to")
    public Date getSellerPickupTo() {
        return sellerPickupTo;
    }

    public void setSellerPickupTo(Date seller_pickup_to) {
        this.sellerPickupTo = seller_pickup_to;
    }

    @JsonSerialize(using = DateSerializer.class)
    @NotNull
    @Basic
    @Temporal(TemporalType.TIME)
    @Column(name = "deadline", nullable = false)
    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    /**
     * Is free shipping possible.
     */
    @Basic
    @Column(name = "seller_delivery_free", nullable = false)
    public boolean isSellerDeliveryFree() {
        return sellerDeliveryFree;
    }

    public void setSellerDeliveryFree(boolean sellerDeliveryFree) {
        this.sellerDeliveryFree = sellerDeliveryFree;
    }

    /**
     * Minimal quantity for free shipping.
     */
    @Basic
    @Column(name = "seller_delivery_min", nullable = false)
    public int getSellerDeliveryMin() {
        return sellerDeliveryMin;
    }

    public void setSellerDeliveryMin(int sellerDeliveryMin) {
        this.sellerDeliveryMin = sellerDeliveryMin;
    }

    /**
     * Is paid shipping possible.
     */
    @Basic
    @Column(name = "seller_delivery_paid", nullable = false)
    public boolean isSellerDeliveryPaid() {
        return sellerDeliveryPaid;
    }

    public void setSellerDeliveryPaid(boolean sellerDeliveryPaid) {
        this.sellerDeliveryPaid = sellerDeliveryPaid;
    }

    /**
     * Cost of paid shipping.
     */
    @Basic
    @Column(name = "seller_delivery_cost")
    public BigDecimal getSellerDeliveryCost() {
        return sellerDeliveryCost;
    }

    public void setSellerDeliveryCost(BigDecimal seller_delivery_cost) {
        this.sellerDeliveryCost = seller_delivery_cost;
    }

    @Min(0)
    @Basic
    @Column(name = "seller_free_replacement")
    public int getSellerFreeReplacement() {
        return sellerFreeReplacement;
    }

    public void setSellerFreeReplacement(int sellerFreeReplacement) {
        this.sellerFreeReplacement = sellerFreeReplacement;
    }

    @Min(0)
    @Basic
    @Column(name = "seller_free_repair")
    public int getSellerFreeRepair() {
        return sellerFreeRepair;
    }

    public void setSellerFreeRepair(int sellerFreeRepair) {
        this.sellerFreeRepair = sellerFreeRepair;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "seller_warranty_additional")
    public String getSellerWarrantyAdditional() {
        return sellerWarrantyAdditional;
    }

    public void setSellerWarrantyAdditional(String sellerWarrantyAdditional) {
        this.sellerWarrantyAdditional = sellerWarrantyAdditional;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "seller_pickup_place_addition")
    public String getSellerPickupPlaceAddition() {
        return sellerPickupPlaceAddition;
    }

    public void setSellerPickupPlaceAddition(String sellerPickupPlaceAddition) {
        this.sellerPickupPlaceAddition = sellerPickupPlaceAddition;
    }

    /**
     * Default place for delivery.
     */
    @Size(max = 255)
    @Basic
    @Column(name = "buyer_delivery_place")
    public String getBuyerDeliveryPlace() {
        return buyerDeliveryPlace;
    }

    public void setBuyerDeliveryPlace(String buyerDeliveryPlace) {
        this.buyerDeliveryPlace = buyerDeliveryPlace;
    }

    /**
     * Default delivery time range from.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "buyer_delivery_from")
    public Date getBuyerDeliveryFrom() {
        return buyerDeliveryFrom;
    }

    public void setBuyerDeliveryFrom(Date buyer_delivery_from) {
        this.buyerDeliveryFrom = buyer_delivery_from;
    }

    /**
     * Default delivery time range to.
     */
    @JsonSerialize(using = DateSerializer.class)
    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "buyer_delivery_to")
    public Date getBuyerDeliveryTo() {
        return buyerDeliveryTo;
    }

    public void setBuyerDeliveryTo(Date buyer_delivery_to) {
        this.buyerDeliveryTo = buyer_delivery_to;
    }

    /**
     * Current region of user.
     */
    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Account balance.
     */
    @Basic
    @Column(name = "balance", nullable = false)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Unique secret token for email confirmation or unsubscribe
     */
    @JsonIgnore
    @Basic
    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Is email confirmed
     */
    @Basic
    @Column(name = "email_valid")
    public boolean isEmailValid() {
        return emailValid;
    }

    public void setEmailValid(boolean emailValidated) {
        this.emailValid = emailValidated;
    }

    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER_ROLE"));
    }

    @Valid
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "modified")
    @OrderBy("modified DESC")
    public Set<MarketplaceFilter> getMarketplaceFilters() {
        return marketplaceFilters;
    }

    public void setMarketplaceFilters(Set<MarketplaceFilter> marketplaceFilters) {
        this.marketplaceFilters = marketplaceFilters;
    }

    @Valid
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "modified")
    @OrderBy("modified DESC")
    public Set<Partner> getPartners() {
        return partners;
    }

    public void setPartners(Set<Partner> partners) {
        this.partners = partners;
    }

}
