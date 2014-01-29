package com.pricegsm.service;

import com.pricegsm.dao.ProductDao;
import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService
        extends GlobalEntityService<Product> {

    @Autowired
    private ProductDao dao;

    @Override
    protected ProductDao getDao() {
        return dao;
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
}
