package com.pricegsm.service;

import com.pricegsm.dao.ProductDao;
import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ProductService
        extends GlobalEntityService<Product> {

    @Autowired
    private ProductDao dao;

    @Override
    protected ProductDao getDao() {
        return dao;
    }

    public List<ProductForm> findByVendor(long vendor) {
        List<Product> byVendor = getDao().findByVendor(vendor);
        return getProductForms(byVendor);
    }

    public List<Product> findActiveByVendorOrderByVendorAndName(long vendor) {
        return postLoad(getDao().findActiveByVendorOrderByVendorAndName(vendor));
    }

    public List<Product> findByYandexId(String yandexId) {
        return postLoad(getDao().findByYandexId(yandexId));
    }

    public List<Color> findColors(String yandexId) {
        return getDao().findColors(yandexId);
    }

    public List<Product> findActive() {
        return getDao().findActive();
    }

    public List<ProductForm> getProductsGroupingByYandexId() {
        List<Product> allProducts = getDao().findAll();

        return getProductForms(allProducts);
    }

    protected List<ProductForm> getProductForms(List<Product> allProducts) {
        Map<String, List<Product>> productsGroupingByYandexId = new LinkedHashMap<>();
        for (Product product : allProducts) {
            if (productsGroupingByYandexId.containsKey(product.getYandexId())) {
                List<Product> products = productsGroupingByYandexId.get(product.getYandexId());
                products.add(product);
            } else {
                ArrayList<Product> productList = new ArrayList<>();
                productList.add(product);
                productsGroupingByYandexId.put(product.getYandexId(), productList);
            }
        }

        return convertToProductFormList(productsGroupingByYandexId);
    }

    private List<ProductForm> convertToProductFormList(
            Map<String, List<Product>> productsGroupingByYandexId) {

        List<ProductForm> productForms = new ArrayList<>();
        for (String yandexId : productsGroupingByYandexId.keySet()) {
            productForms.add(new ProductForm(productsGroupingByYandexId.get(yandexId)));
        }

        return productForms;
    }

    public ProductForm getProductForm(String yandexId) {
        List<Product> products = getDao().findByYandexId(yandexId);
        return new ProductForm(products);
    }

    @Transactional
    public void saveProductFromProductForm(ProductForm productForm) {
        for (Product product : productForm.convertToProducts()) {
            save(product);
        }
    }

    public DeleteResult deleteProductsByYandexId(String yandexId) {
        getDao().deleteProductsByYandexId(yandexId);
        return DeleteResult.OK;
    }
}
