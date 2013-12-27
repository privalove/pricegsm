package com.pricegsm.service;

import com.pricegsm.dao.ProductDao;
import com.pricegsm.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService
        extends GlobalEntityService<Product> {

    @Autowired
    private ProductDao dao;

    @Override
    protected ProductDao getDao() {
        return dao;
    }
}
