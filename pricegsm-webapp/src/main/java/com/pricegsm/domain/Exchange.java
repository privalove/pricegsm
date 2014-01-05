package com.pricegsm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.jackson.GlobalEntityDeserializer;
import com.pricegsm.jackson.GlobalEntitySerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "exchange")
public class Exchange
        extends GlobalEntity {

    private Currency from;

    private Currency to;

    private Date date;

    private BigDecimal value = BigDecimal.ONE;

    public Exchange() {
    }

    public Exchange(Currency from, Currency to, Date date, BigDecimal value) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.value = value;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Exchange")
    @SequenceGenerator(name = "Exchange", sequenceName = "exchange_seq", allocationSize = 1)
    @Override
    public long getId() {
        return super.getId();
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "from_currency_id", referencedColumnName = "id", nullable = false)
    public Currency getFrom() {
        return from;
    }

    public void setFrom(Currency from) {
        this.from = from;
    }

    @JsonSerialize(using = GlobalEntitySerializer.class)
    @JsonDeserialize(using = GlobalEntityDeserializer.class)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "to_currency_id", referencedColumnName = "id", nullable = false)
    public Currency getTo() {
        return to;
    }

    public void setTo(Currency to) {
        this.to = to;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Basic
    @Column(name = "exchange_date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NotNull
    @Basic
    @Column(name = "value", nullable = false)
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
