package com.pricegsm.service;

import com.pricegsm.dao.RegionDao;
import com.pricegsm.domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionService
        extends GlobalEntityService<Region> {

    @Autowired
    private RegionDao dao;

    @Override
    protected RegionDao getDao() {
        return dao;
    }
}
