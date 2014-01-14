package com.pricegsm.dao;

import com.pricegsm.domain.PriceList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PriceListDao
        extends GlobalEntityDao<PriceList> {
    public List<PriceList> findForUser(long userId) {
        return getEntityManager()
                .createQuery("select p from PriceList p where p.user.id = :userId")
                .setParameter("userId", userId)
                .getResultList();

    }
}
