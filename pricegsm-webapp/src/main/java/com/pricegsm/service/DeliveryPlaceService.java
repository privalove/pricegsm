package com.pricegsm.service;

import com.pricegsm.dao.DeliveryPlaceDao;
import com.pricegsm.domain.DeliveryPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPlaceService
        extends GlobalEntityService<DeliveryPlace> {

    @Autowired
    private DeliveryPlaceDao dao;

    @Override
    protected DeliveryPlaceDao getDao() {
        return dao;
    }
}
