package com.pricegsm.config;

import org.junit.Before;
import org.springframework.security.web.FilterChainProxy;

import javax.inject.Inject;
import javax.inject.Named;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class WebSecurityConfigurationAware extends WebAppConfigurationAware {

    @Inject
    @Named("defaultHttp")
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }
}
