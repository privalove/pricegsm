package com.pricegsm.integration;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import com.pricegsm.domain.Color;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test data db/migration/V1_0_20140201_100__color_test_data.sql
 */
@FlywayTest
public class ColorIntegrationTest extends WebAppConfigurationAware {

    @Test
    public void testViewColor() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/color/100"))
                .andReturn();

        assertEquals(result.getModelAndView().getViewName(), "admin/color");
        assertNotNull(result.getModelAndView().getModel().get("color"));
        assertEquals(((Color) result.getModelAndView().getModel().get("color")).getId(), 100L);
    }
}
