package com.pricegsm.service;

import com.pricegsm.dao.ProductTypeDao;
import com.pricegsm.domain.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService
        extends GlobalEntityService<ProductType> {

    @Autowired
    private ProductTypeDao dao;

    @Override
    protected ProductTypeDao getDao() {
        return dao;
    }

    public List<ProductType> findActive()  {
        return getDao().findActive();
    }
}
