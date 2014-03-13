package com.pricegsm.service;

import com.pricegsm.dao.ProductDao;
import com.pricegsm.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: o.logunov
 * Date: 15.02.14
 * Time: 0:08
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    private List<Product> savedProducts = new ArrayList<>();
    private List<Long> deletedProductId = new ArrayList<>();
    private List<Product> convertedProducts = new ArrayList<>();

    @InjectMocks
    ProductService productService = new ProductService() {
        @Override
        public SaveResult<Product> save(Product entity) {
            savedProducts.add(entity);
            return null;
        }

        @Override
        public DeleteResult delete(Long id) {
            deletedProductId.add(id);
            return null;
        }

        @Override
        protected List<ProductForm> getProductForms(List<Product> allProducts) {
            convertedProducts.addAll(allProducts);
            return super.getProductForms(allProducts);
        }
    };

    @Mock
    private ProductDao productDao;

    @Test
    public void testFindByVendor() {
        // Given
        Product expectedProduct11 = new Product();
        expectedProduct11.setId(200L);
        expectedProduct11.setYandexId("123456");
        Product expectedProduct12 = new Product();
        expectedProduct12.setId(201L);
        expectedProduct12.setYandexId("123456");

        Product expectedProduct21 = new Product();
        expectedProduct21.setId(200L);
        expectedProduct21.setYandexId("654321");
        Product expectedProduct22 = new Product();
        expectedProduct22.setId(201L);
        expectedProduct22.setYandexId("654321");

        List<Product> expectedProducts =
                Arrays.asList(expectedProduct11, expectedProduct12, expectedProduct21, expectedProduct22);

        when(productDao.findByVendor(123L)).thenReturn(expectedProducts);

        // When
        productService.findByVendor(123L);

        // Then
        assertThat(convertedProducts).isEqualTo(expectedProducts);
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

    @Test
    public void testGetProductsGroupingByYandexId() {
        // Given
        Color color11 = new Color(11L);
        Color color12 = new Color(12L);
        ProductType type1 = new ProductType(1L);
        Vendor vendor1 = new Vendor(1L);

        Product product11 = new Product(
                200L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery11", type1,
                vendor1, color11, true, "description1");

        Product product12 = new Product(
                201L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery12", type1,
                vendor1, color12, true, "description1");

        Color color21 = new Color(21L);
        Color color22 = new Color(22L);
        ProductType type2 = new ProductType(2L);
        Vendor vendor2 = new Vendor(2L);

        Product product21 = new Product(
                202L, "prod2", "654321", "searchQuery2",
                "excludeQuery2", "colorQuery21", type2,
                vendor2, color21, true, "description2");

        Product product22 = new Product(
                203L, "prod2", "654321", "searchQuery2",
                "excludeQuery2", "colorQuery22", type2,
                vendor2, color22, true, "description2");

        List<Product> expectedProducts =
                Arrays.asList(product11, product12, product21, product22);

        when(productDao.findAll()).thenReturn(expectedProducts);

        // When
        List<ProductForm> productsGroupingByYandexId = productService.getProductsGroupingByYandexId();

        // Then
        ColorProductForm colorProductForm11 = new ColorProductForm(200L, new Color(11L), "colorQuery11");
        ColorProductForm colorProductForm12 = new ColorProductForm(201L, new Color(12L), "colorQuery12");
        ProductForm productForm1 =
                new ProductForm(
                        "123456", "prod1", "searchQuery1", "excludeQuery1",
                        new ProductType(1L), new Vendor(1L), true, "description1",
                        Arrays.asList(colorProductForm11, colorProductForm12));

        ColorProductForm colorProductForm21 = new ColorProductForm(202L, new Color(21L), "colorQuery21");
        ColorProductForm colorProductForm22 = new ColorProductForm(203L, new Color(22L), "colorQuery22");
        ProductForm productForm2 =
                new ProductForm(
                        "654321", "prod2", "searchQuery2", "excludeQuery2",
                        new ProductType(2L), new Vendor(2L), true, "description2",
                        Arrays.asList(colorProductForm21, colorProductForm22));

        assertThat(productsGroupingByYandexId).isEqualTo(Arrays.asList(productForm1, productForm2));
    }

    @Test
    public void testGetProductForm() {
        // Given
        Color color11 = new Color(11L);
        Color color12 = new Color(12L);
        ProductType type1 = new ProductType(1L);
        Vendor vendor1 = new Vendor(1L);

        Product product11 = new Product(
                200L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery11", type1,
                vendor1, color11, true, "description1");

        Product product12 = new Product(
                201L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery12", type1,
                vendor1, color12, true, "description1");


        List<Product> expectedProducts =
                Arrays.asList(product11, product12);

        when(productDao.findByYandexId("123456")).thenReturn(expectedProducts);

        // When
        ProductForm productForm = productService.getProductForm("123456");

        // Then
        ColorProductForm colorProductForm11 = new ColorProductForm(200L, new Color(11L), "colorQuery11");
        ColorProductForm colorProductForm12 = new ColorProductForm(201L, new Color(12L), "colorQuery12");
        ProductForm expectedProductForm =
                new ProductForm(
                        "123456", "prod1", "searchQuery1", "excludeQuery1",
                        new ProductType(1L), new Vendor(1L), true, "description1",
                        Arrays.asList(colorProductForm11, colorProductForm12));

        assertThat(productForm).isEqualTo(expectedProductForm);
    }

    @Test
    public void testSaveProductFromProductForm() {
        // Given
        Color color11 = new Color(11L);
        Color color12 = new Color(12L);
        ProductType type1 = new ProductType(1L);
        Vendor vendor1 = new Vendor(1L);

        Product expectedProduct1 = new Product(
                200L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery11", type1,
                vendor1, color11, true, "description1");

        Product expectedProduct2 = new Product(
                201L, "prod1", "123456", "searchQuery1",
                "excludeQuery1", "colorQuery12", type1,
                vendor1, color12, true, "description1");

        when(productDao.findByYandexId("123456")).thenReturn(
                Arrays.asList(expectedProduct1, expectedProduct2));

        ColorProductForm colorProductForm11 = new ColorProductForm(200L, new Color(11L), "colorQuery11");
        ColorProductForm colorProductForm12 = new ColorProductForm(201L, new Color(12L), "colorQuery12");
        ProductForm productForm =
                new ProductForm(
                        "123456", "prod1", "searchQuery1", "excludeQuery1",
                        new ProductType(1L), new Vendor(1L), true, "description1",
                        Arrays.asList(colorProductForm11, colorProductForm12));

        // When
        productService.saveProductFromProductForm(productForm);

        // Then
        assertThat(savedProducts.size()).isEqualTo(2);
        assertThat(savedProducts.get(0)).isEqualTo(expectedProduct1);
        assertThat(savedProducts.get(1)).isEqualTo(expectedProduct2);
    }

    @Test
    public void testDeleteProductsByYandexId() {
        // When
        DeleteResult deleteResult = productService.deleteProductsByYandexId("135246");

        // Then
        verify(productDao).deleteProductsByYandexId("135246");
        assertThat(deleteResult).isEqualTo(DeleteResult.OK);

    }
}
