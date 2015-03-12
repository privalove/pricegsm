package com.pricegsm.dao;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.DaoTest;
import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: o.logunov
 * Date: 13.02.14
 * Time: 23:33
 */
@DaoTest
@FlywayTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    @Test
    public void testGetOrderByProperties() {
        // When
        Iterable<String> properties = productDao.getOrderByProperties();

        // Then
        Iterator<String> iterator = properties.iterator();
        assertThat(iterator.next()).isEqualTo("vendor.id");
        assertThat(iterator.next()).isEqualTo("type.id");
        assertThat(iterator.next()).isEqualTo("name");
        assertThat(iterator.next()).isEqualTo("color.id");
    }

    @Test
    public void testFindByYandexId() {
        // When
        List<Product> products = productDao.findByYandexId("10495459");
        // Then
        Product expectedProduct = new Product();
        expectedProduct.setId(200L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(201L);
        assertThat(products).isEqualTo(Arrays.asList(expectedProduct, expectedProduct1));
    }

    @Test
    public void testFindByVendor() {
        // When
        List<Product> products = productDao.findByVendor(201L);
        // Then
        Product expectedProduct = new Product();
        expectedProduct.setId(209L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(210L);
        assertThat(products).isEqualTo(Arrays.asList(expectedProduct, expectedProduct1));
    }

    @Test
    public void testFindActiveByVendorOrderByVendorAndName() {
        // When
        List<Product> products =
                productDao.findActiveByVendorOrderByVendorAndName(200, Arrays.asList(200L, 201L));

        // Then
        Product expectedProduct = new Product();
        expectedProduct.setId(206L);
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(205L);
        Product expectedProduct2 = new Product();
        expectedProduct2.setId(204L);
        Product expectedProduct3 = new Product();
        expectedProduct3.setId(203L);

        assertThat(products).isEqualTo(Arrays.asList(expectedProduct, expectedProduct1, expectedProduct2, expectedProduct3));
    }

    @Test
    public void testFindColors() {
        // When
        List<Color> products = productDao.findColors("10495461");

        // Then
        Color expectedColor = new Color();
        expectedColor.setId(200L);
        Color expectedColor1 = new Color();
        expectedColor1.setId(201L);

        assertThat(products).isEqualTo(Arrays.asList(expectedColor, expectedColor1));
    }

    @Test
    public void testDeleteProductsByYandexId() {
        // Given
        assertThat(productDao.findByYandexId("10495462").size()).isEqualTo(2);
        // When
        productDao.deleteProductsByYandexId("10495462");
        // Then
        assertThat(productDao.findByYandexId("10495462").size()).isEqualTo(0);
    }
}
