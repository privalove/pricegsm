package com.pricegsm.domain;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "persistent_logins")
public class Token
        implements Serializable {

    private String series;
    private String username;
    private String tokenValue;
    private Date date;

    public Token() {
    }

    public Token(PersistentRememberMeToken token) {
        setSeries(token.getSeries());
        setDate(token.getDate());
        setTokenValue(token.getTokenValue());
        setUsername(token.getUsername());
    }

    public PersistentRememberMeToken toToken() {
        return new PersistentRememberMeToken(getUsername(), getSeries(), getTokenValue(), getDate());
    }

    @Id
    @Column(name = "series", nullable = false)
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Basic
    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "token", nullable = false)
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_used", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
