package com.pricegsm.controller.admin;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import com.pricegsm.domain.*;
import com.pricegsm.support.web.Message;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
        ProductForm productForm = new ProductForm(
                "0", null, null, null, null,
                null, null, true, null,
                Arrays.asList(new ColorProductForm(0L, null, null, null)), false);

        mockMvc.perform(get("/admin/product/0/4"))
                .andExpect(model().attribute("productForm", productForm))
                .andExpect(model().attribute("vendorId", 4L))
                .andExpect(view().name("admin/product"));
    }

    @Test
    public void testEditProduct() throws Exception {
        ColorProductForm colorProductForm1 = new ColorProductForm(102L, new Color(200L), "colorQuery1", null);
        ColorProductForm colorProductForm2 = new ColorProductForm(1021L, new Color(201L), "colorQuery2", null);
        ProductForm productForm =
                new ProductForm(
                        "10495458", "product 3", "search_query", "search_pl_query", "exclude_query",
                        new ProductType(200L), new Vendor(100L), true, "description",
                        Arrays.asList(colorProductForm1, colorProductForm2), false);

        mockMvc.perform(get("/admin/product/10495458/4"))
                .andExpect(model().attribute("productForm", productForm))
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
    public void testAddColor() throws Exception {
        ColorProductForm colorProductForm1 = new ColorProductForm(102L, new Color(200L), "colorQuery1", "excludedColorQuery1");
        ColorProductForm colorProductForm2 = new ColorProductForm(1021L, new Color(201L), "colorQuery2", "excludedColorQuery2");
        ColorProductForm colorProductForm3 = new ColorProductForm(0L, new Color(1L), "", "");
        ProductForm productForm =
                new ProductForm(
                        "10495458", "product 3", "search_query", "search_Price_List_Query", "exclude_query",
                        new ProductType(3L), new Vendor(8L), true, "desc",
                        Arrays.asList(colorProductForm1, colorProductForm2, colorProductForm3), false);


        mockMvc.perform(post("/admin/product/45495457/10")
                .param("addColor", "")
                .param("yandexId", "10495458")
                .param("name", "product 3")
                .param("searchQuery", "search_query")
                .param("searchPriceListQuery", "search_Price_List_Query")
                .param("excludeQuery", "exclude_query")
                .param("vendor", "8")
                .param("type", "3")
                .param("active", "true")
                .param("manufacturerWarranty", "false")
                .param("description", "desc")
                .param("colors[0].productId", "102")
                .param("colors[0].color", "200")
                .param("colors[0].colorQuery", "colorQuery1")
                .param("colors[0].excludedColorQuery", "excludedColorQuery1")
                .param("colors[1].productId", "1021")
                .param("colors[1].color", "201")
                .param("colors[1].colorQuery", "colorQuery2")
                .param("colors[1].excludedColorQuery", "excludedColorQuery2")
        ).andDo(print()).andExpect(view().name("admin/product"))
                .andExpect(model().attribute("productForm", productForm))
                .andExpect(model().attribute("vendorId", 10L));
    }

    @Test
    public void testRemoveProductByColorRemovingFromDB() throws Exception {
        ColorProductForm colorProductForm = new ColorProductForm(1022L, new Color(200L), "colorQuery1", null);
        ProductForm productForm =
                new ProductForm(
                        "20495459", "product 3", "search_query", "search_pl_query", "exclude_query",
                        new ProductType(200L), new Vendor(100L), true, "description",
                        Arrays.asList(colorProductForm), false);

        mockMvc.perform(post("/admin/product/20495459/10").param("removeColor", "1")
                .param("yandexId", "20495459")
                .param("name", "product 3")
                .param("searchQuery", "search_query")
                .param("excludeQuery", "exclude_query")
                .param("searchPriceListQuery", "search_pl_query")
                .param("vendor", "100")
                .param("type", "200")
                .param("active", "true")
                .param("description", "description")
                .param("colors[0].productId", "1022")
                .param("colors[0].color", "200")
                .param("colors[0].colorQuery", "colorQuery1")
                .param("colors[1].productId", "1023")
                .param("colors[1].color", "201")
                .param("colors[1].colorQuery", "colorQuery2")
        ).andExpect(view().name("admin/product"))
                .andExpect(model().attribute("productForm", productForm))
                .andExpect(model().attribute("vendorId", 10L))
                .andExpect(model().attribute("message", new Message("alert.deleted", Message.Type.SUCCESS, new Object[0])));

        mockMvc.perform(get("/admin/product/20495459/10"))
                .andExpect(model().attribute("productForm", productForm));
    }

    @Test
    public void testRemoveJustAddedColor() throws Exception {
        ColorProductForm colorProductForm1 = new ColorProductForm(1024L, new Color(200L), "colorQuery1", null);
        ColorProductForm colorProductForm2 = new ColorProductForm(1025L, new Color(201L), "colorQuery2", null);
        ProductForm productForm =
                new ProductForm(
                        "20495460", "product 3", "search_query", "search_pl_query", "exclude_query",
                        new ProductType(200L), new Vendor(100L), true, "description",
                        Arrays.asList(colorProductForm1, colorProductForm2), false);

        mockMvc.perform(post("/admin/product/20495460/10").param("removeColor", "2")
                .param("yandexId", "20495460")
                .param("name", "product 3")
                .param("searchQuery", "search_query")
                .param("searchPriceListQuery", "search_pl_query")
                .param("excludeQuery", "exclude_query")
                .param("vendor", "100")
                .param("type", "200")
                .param("active", "true")
                .param("description", "description")
                .param("colors[0].productId", "1024")
                .param("colors[0].color", "200")
                .param("colors[0].colorQuery", "colorQuery1")
                .param("colors[1].productId", "1025")
                .param("colors[1].color", "201")
                .param("colors[1].colorQuery", "colorQuery2")
                .param("colors[2].productId", "0")
                .param("colors[2].color", "1")
                .param("colors[2].colorQuery", "colorQuery2")

        ).andExpect(view().name("admin/product"))
                .andExpect(model().attribute("productForm", productForm))
                .andExpect(model().attribute("vendorId", 10L))
                .andExpect(model().attribute("message", (Object) null));

        mockMvc.perform(get("/admin/product/20495460/10"))
                .andExpect(model().attribute("productForm", productForm));
    }

    @Test
    public void testSaveProductWithError() throws Exception {
        mockMvc.perform(post("/admin/product/1000/0").param("vendor", "err"))
                .andExpect(view().name("admin/product"));
    }


    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/product/104954561/10/delete"))
                .andExpect(view().name("redirect:/admin/product/10/vendor"))
                .andExpect(flash().attribute("message", new Message("alert.deleted", Message.Type.SUCCESS, new Object[0])));
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
                        List<ProductForm> products = (List) result.getModelAndView().getModel().get("products");
                        for (ProductForm product : products) {
                            assertEquals("Check vendor id", 100L, product.getVendor().getId());
                        }
                    }
                });
    }
}
