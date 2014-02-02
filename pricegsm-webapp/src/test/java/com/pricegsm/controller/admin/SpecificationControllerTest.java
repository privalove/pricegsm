package com.pricegsm.controller.admin;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import com.pricegsm.domain.Specification;
import com.pricegsm.support.web.Message;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: o.logunov
 * Date: 31.01.14
 * Time: 22:25
 */
@FlywayTest
public class SpecificationControllerTest extends WebAppConfigurationAware {

    @Test
    public void testSpecification() throws Exception {
        mockMvc.perform(get("/admin/specification"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        assertTrue("Model", !result.getModelAndView().getModel().isEmpty());
                    }
                })
                .andExpect(view().name("admin/specifications"));
    }

    @Test
    public void testNewSpecification() throws Exception {
        Specification specification = new Specification();
        specification.setId(0);

        mockMvc.perform(get("/admin/specification/0"))
                .andExpect(model().attribute("specification", specification))
                .andExpect(view().name("admin/specification"));
    }

    @Test
    public void testEditSpecification() throws Exception {
        Specification specification = new Specification();
        specification.setId(102);

        mockMvc.perform(get("/admin/specification/102"))
                .andExpect(model().attribute("specification", specification))
                .andExpect(view().name("admin/specification"));
    }

    @Test
    public void testSaveSpecificationNew() throws Exception {
        mockMvc.perform(post("/admin/specification/0")
                .param("id", "0")
                .param("active", "true")
                .param("name", "specification")
                .param("description", "description description"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.saved.she", Message.Type.SUCCESS, new Object[0])));
    }

    @Test
    public void testSaveSpecificationUpdate() throws Exception {

        // Update description
        mockMvc.perform(post("/admin/specification/101")
                .param("id", "101")
                .param("active", "true")
                .param("name", "spec")
                .param("description", "new description"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.saved.she", Message.Type.SUCCESS, new Object[0])));

        // Update name
        mockMvc.perform(post("/admin/specification/101")
                .param("id", "101")
                .param("active", "true")
                .param("name", "new spec")
                .param("description", "new description"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.saved.she", Message.Type.SUCCESS, new Object[0])));

        // Update active status
        mockMvc.perform(post("/admin/specification/101")
                .param("id", "101")
                .param("active", "false")
                .param("name", "new spec")
                .param("description", "new description"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.saved.she", Message.Type.SUCCESS, new Object[0])));

        // All fields
        mockMvc.perform(post("/admin/specification/101")
                .param("id", "101")
                .param("active", "true")
                .param("name", "spec")
                .param("description", "description"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.saved.she", Message.Type.SUCCESS, new Object[0])));
    }


    @Test
    public void testSaveSpecificationWithError() throws Exception {
        mockMvc.perform(post("/admin/specification/1000").param("aaa", "aaa"))
                .andExpect(view().name("admin/specification"));
    }


    @Test
    public void testDeleteSpecification() throws Exception {
        mockMvc.perform(delete("/admin/specification/100/delete"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", new Message("alert.deleted.she", Message.Type.SUCCESS, new Object[0])));
    }

    @Test
    public void testDeleteSpecificationFailed() throws Exception {
        mockMvc.perform(delete("/admin/specification/1000/delete"))
                .andExpect(view().name("redirect:/admin/specification"))
                .andExpect(flash().attribute("message", (Object) null));
    }
}
