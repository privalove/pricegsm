package com.pricegsm.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "currency")
public class Currency
        extends GlobalEntity {

    public static final long USD = 1L;

    public static final long EUR = 2L;

    public static final long RUB = 2L;

    private String name;

    private String code;

    private String symbol;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "Currency")
    @SequenceGenerator(name = "Currency", sequenceName = "currency_seq", allocationSize = 1)
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

    @Size(max = 255)
    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

