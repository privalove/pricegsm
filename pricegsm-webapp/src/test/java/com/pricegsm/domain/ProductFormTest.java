package com.pricegsm.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: o.logunov
 * Date: 23.02.14
 * Time: 1:40
 */
public class ProductFormTest {
    @Test
    public void testCreate() {
        // Given
        Color color11 = new Color(1L);
        Color color12 = new Color(2L);
        ProductType type1 = new ProductType(1L);
        Vendor vendor1 = new Vendor(1L);

        Product expectedProduct11 = new Product(
                200L, "prod1", "123456", "searchQuery1",
                "search_Price_List_Query", "excludeQuery1", "colorQuery1", type1,
                vendor1, color11, true, "description1");

        Product expectedProduct12 = new Product(
                201L, "prod1", "123456", "searchQuery1",
                "search_Price_List_Query", "excludeQuery1", "colorQuery2", type1,
                vendor1, color12, true, "description1");

        // When
        ProductForm productForm =
                new ProductForm(Arrays.asList(expectedProduct11, expectedProduct12));

        // Then
        ColorProductForm colorProductForm11 = new ColorProductForm(200L, new Color(1L), "colorQuery1");
        ColorProductForm colorProductForm12 = new ColorProductForm(201L, new Color(2L), "colorQuery2");
        ProductForm expectedProductForm =
                new ProductForm(
                        "123456", "prod1", "searchQuery1", "search_Price_List_Query", "excludeQuery1",
                        new ProductType(1L), new Vendor(1L), true, "description1",
                        Arrays.asList(colorProductForm11, colorProductForm12));

        assertThat(productForm).isEqualTo(expectedProductForm);
    }

    @Test
    public void testConvertToProducts() {
        // Given
        ColorProductForm colorProductForm11 = new ColorProductForm(200L, new Color(1L), "colorQuery1");
        ColorProductForm colorProductForm12 = new ColorProductForm(201L, new Color(2L), "colorQuery2");
        ProductForm productForm =
                new ProductForm(
                        "123456", "prod1", "searchQuery1", "search_Price_List_Query", "excludeQuery1",
                        new ProductType(1L), new Vendor(1L), true, "description1",
                        Arrays.asList(colorProductForm11, colorProductForm12));

        // When
        List<Product> products = productForm.convertToProducts();

        // Then
        Color color11 = new Color(11L);
        Color color12 = new Color(12L);
        ProductType type1 = new ProductType(1L);
        Vendor vendor1 = new Vendor(1L);

        Product expectedProduct1 = new Product(
                200L, "prod1", "123456", "searchQuery1",
                "search_Price_List_Query", "excludeQuery1", "colorQuery11", type1,
                vendor1, color11, true, "description1");

        Product expectedProduct2 = new Product(
                201L, "prod1", "123456", "searchQuery1",
                "search_Price_List_Query", "excludeQuery1", "colorQuery12", type1,
                vendor1, color12, true, "description1");

        assertThat(products).isEqualTo(Arrays.asList(expectedProduct1, expectedProduct2));

    }
}
