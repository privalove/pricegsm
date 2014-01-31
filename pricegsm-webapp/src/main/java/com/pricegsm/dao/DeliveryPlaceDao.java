package com.pricegsm.dao;

import com.pricegsm.domain.DeliveryPlace;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliveryPlaceDao
        extends GlobalEntityDao<DeliveryPlace> {

    public List<DeliveryPlace> findActiveByRegion(long region) {
        return getEntityManager()
                .createQuery("select d from DeliveryPlace d where d.active = true and d.region.id = :region order by d.name")
                .setParameter("region", region)
                .getResultList();
    }
}
