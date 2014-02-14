package com.pricegsm.service;

import com.pricegsm.dao.ProductDao;
import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: o.logunov
 * Date: 15.02.14
 * Time: 0:08
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService = new ProductService();

    @Mock
    private ProductDao productDao;

    @Test
    public void testFindByVendor() {
        // Given
        Product expectedProduct = new Product();
        expectedProduct.setId(200L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(201L);
        List<Product> expectedProducts = Arrays.asList(expectedProduct, expectedProduct1);

        when(productDao.findByVendor(123L)).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.findByVendor(123L);

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    public void testFindActiveByVendorOrderByVendorAndName() {
        // Given
        Product expectedProduct = new Product();
        expectedProduct.setId(200L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(201L);
        List<Product> expectedProducts = Arrays.asList(expectedProduct, expectedProduct1);

        when(productDao.findActiveByVendorOrderByVendorAndName(4L)).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.findActiveByVendorOrderByVendorAndName(4L);

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    public void testFindByYandexId() {
        // Given
        Product expectedProduct = new Product();
        expectedProduct.setId(200L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(201L);
        List<Product> expectedProducts = Arrays.asList(expectedProduct, expectedProduct1);

        when(productDao.findByYandexId("123456")).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.findByYandexId("123456");

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    public void testFindColors() {
        // Given
        Color expectedColor = new Color();
        expectedColor.setId(200L);
        Color expectedColor1 = new Color();
        expectedColor1.setId(201L);
        List<Color> expectedColors = Arrays.asList(expectedColor, expectedColor1);

        when(productDao.findColors("123456")).thenReturn(expectedColors);

        // When
        List<Color> actualColors = productService.findColors("123456");

        // Then
        assertThat(actualColors).isEqualTo(expectedColors);
    }

    @Test
    public void testFindActive() {
        Product expectedProduct = new Product();
        expectedProduct.setId(200L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(201L);
        List<Product> expectedProducts = Arrays.asList(expectedProduct, expectedProduct1);

        when(productDao.findActive()).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.findActive();

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }
}
