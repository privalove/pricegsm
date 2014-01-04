package com.pricegsm.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service for both type of users administrators and typical.
 */
public interface BaseUserService
        extends UserDetailsService {
}
