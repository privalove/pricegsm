package com.pricegsm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Build {

    @Value("${build.profile}")
    private String profile;

    public String getProfile() {
        return profile;
    }
}
