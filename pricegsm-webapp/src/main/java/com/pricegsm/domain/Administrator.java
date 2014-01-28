package com.pricegsm.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Administrator of the application.
 */
@Entity
@Table(name="administrator")
@PrimaryKeyJoinColumn(name="id", referencedColumnName="id")
public class Administrator
        extends BaseUser {

    public Administrator() {
    }

    public Administrator(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }


    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER_ROLE"), new SimpleGrantedAuthority("ADMIN_ROLE"));
    }
}
