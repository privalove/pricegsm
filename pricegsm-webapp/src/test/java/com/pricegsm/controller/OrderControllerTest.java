package com.pricegsm.controller;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import com.pricegsm.config.WebSecurityConfigurationAware;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * User: o.logunov
 * Date: 08.04.14
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
@FlywayTest
public class OrderControllerTest extends WebSecurityConfigurationAware {
    @Test
    public void testOrder() throws Exception{
        mockMvc.perform(get("/order"));
    }

    @Test
    public void testOrders() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/order/orders.json")).andReturn();
    }
}
