package com.pricegsm.controller.admin;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import com.pricegsm.domain.Product;
import com.pricegsm.support.web.Message;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: o.logunov
 * Date: 13.02.14
 * Time: 22:34
 */
@FlywayTest
public class ProductControllerTest extends WebAppConfigurationAware {

    @Test
    public void testProduct() throws Exception {
        mockMvc.perform(get("/admin/product"))
                .andExpect(notEmptyModelMatcher())
                .andExpect(notEmptyValuesOfAttributeMatcher("types"))
                .andExpect(notEmptyValuesOfAttributeMatcher("colors"))
                .andExpect(notEmptyValuesOfAttributeMatcher("vendors"))
                .andExpect(notEmptyValuesOfAttributeMatcher("products"))
                .andExpect(model().attribute("vendorId", 0))
                .andExpect(view().name("admin/products"));
    }

    private ResultMatcher notEmptyModelMatcher() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                assertTrue("Model", !result.getModelAndView().getModel().isEmpty());
            }
        };
    }

    private ResultMatcher notEmptyValuesOfAttributeMatcher(final String attribute) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                List values = (List) result.getModelAndView().getModel().get(attribute);
                assertTrue(attribute, !values.isEmpty());
            }
        };
    }

    @Test
    public void testNewProduct() throws Exception {
        Product product = new Product();
        product.setId(0);

        mockMvc.perform(get("/admin/product/0/4"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attribute("vendorId", 4L))
                .andExpect(view().name("admin/product"));
    }

    @Test
    public void testEditProduct() throws Exception {
        Product product = new Product();
        product.setId(102);

        mockMvc.perform(get("/admin/product/102/4"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attribute("vendorId", 4L))
                .andExpect(view().name("admin/product"));
    }

    @Test
    public void testSaveProductNew() throws Exception {
        mockMvc.perform(post("/admin/product/0/0")
                .param("id", "0")
                .param("active", "true")
                .param("name", "product")
                .param("color", "1")
                .param("yandexId", "2")
                .param("vendor", "4")
                .param("type", "1"))
                .andExpect(view().name("redirect:/admin/product"))
                .andExpect(flash().attribute("message", new Message("alert.saved", Message.Type.SUCCESS, new Object[0])));
    }

    @Test
    public void testSaveProductUpdate() throws Exception {

        // Update all fields
        mockMvc.perform(post("/admin/product/101/0")
                .param("id", "101")
                .param("name", "new product")
                .param("color", "7")
                .param("yandexId", "45495457")
                .param("vendor", "8")
                .param("type", "3"))
                .andExpect(view().name("redirect:/admin/product"))
                .andExpect(flash().attribute("message", new Message("alert.saved", Message.Type.SUCCESS, new Object[0])));
    }

    @Test
    public void testSaveFilterSettingDuringUpdate() throws Exception {

        mockMvc.perform(post("/admin/product/101/10")
                .param("id", "101")
                .param("name", "new product")
                .param("color", "7")
                .param("yandexId", "45495457")
                .param("vendor", "8")
                .param("type", "3"))
                .andExpect(view().name("redirect:/admin/product/10/vendor"))
                .andExpect(flash().attribute("message", new Message("alert.saved", Message.Type.SUCCESS, new Object[0])));
    }


    @Test
    public void testSaveProductWithError() throws Exception {
        mockMvc.perform(post("/admin/product/1000/0").param("aaa", "aaa"))
                .andExpect(view().name("admin/product"));
    }


    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/product/100/delete"))
                .andExpect(view().name("redirect:/admin/product"))
                .andExpect(flash().attribute("message", new Message("alert.deleted", Message.Type.SUCCESS, new Object[0])));
    }

    @Test
    public void testDeleteProductFailed() throws Exception {
        mockMvc.perform(delete("/admin/product/1000/delete"))
                .andExpect(view().name("redirect:/admin/product"))
                .andExpect(flash().attribute("message", (Object) null));
    }

    @Test
    public void testProductsByVendor() throws Exception {
        mockMvc.perform(get("/admin/product/100/vendor"))
                .andExpect(notEmptyModelMatcher())
                .andExpect(model().attribute("vendorId", 100L))
                .andExpect(view().name("admin/products"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        List<Product> products = (List) result.getModelAndView().getModel().get("products");
                        for (Product product : products) {
                            assertEquals("Check vendor id", 100L, product.getVendor().getId());
                        }
                    }
                });
    }
}
