package com.pricegsm.service;

import com.pricegsm.dao.SpecificationDao;
import com.pricegsm.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService
        extends GlobalEntityService<Specification> {

    @Autowired
    private SpecificationDao dao;

    @Override
    protected SpecificationDao getDao() {
        return dao;
    }

    public List<Specification> findActive() {
        return getDao().findActive();
    }
}
