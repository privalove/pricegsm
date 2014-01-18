package com.pricegsm.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Base class for typical users and administrators.
 */
@Entity
@Table(name = "base_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseUser
        extends GlobalEntity
        implements UserDetails, Activable {

    protected boolean active = true;

    protected String name;

    protected String email;

    protected String password;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "BaseUser")
    @SequenceGenerator(name = "BaseUser", sequenceName = "base_user_seq", allocationSize = 1)
    public long getId() {
        return super.getId();
    }

    /**
     * Email is being used as login.
     */
    @NotBlank
    @Size(max = 255)
    @Basic
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    @Size(max = 255)
    @Basic
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Name of the user which is visible for his partners.
     */
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

    @Basic
    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public String getUsername() {
        return getEmail();
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
