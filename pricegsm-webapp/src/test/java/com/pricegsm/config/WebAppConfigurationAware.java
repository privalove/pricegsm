package com.pricegsm.config;

import com.googlecode.flyway.test.junit.FlywayTestExecutionListener;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(classes = {
        ApplicationConfig.class, FlywayConfig.class, JpaConfig.class,
        SecurityConfig.class, WebMvcConfig.class, TestPrincipalHolder.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class })
public abstract class WebAppConfigurationAware {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

}
