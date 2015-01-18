package com.pricegsm.dao;

import com.pricegsm.domain.PriceList;
import com.pricegsm.domain.PriceListPK;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class PriceListDao
        extends BaseEntityDao<PriceList, PriceListPK> {
    public List<PriceList> findForUser(long userId) {
        return getEntityManager()
                .createQuery("select p from PriceList p where p.user.id = :userId")
                .setParameter("userId", userId)
                .getResultList();

    }

    public Date getLastPriceList(long userId) {
        return (Date) getEntityManager()
                .createQuery("select p.sellFromDate from PriceList p " +
                        "where p.user.id = :userId " +
                        " and p.position = 0")
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public List<PriceList> getAccessiblePriceLists(long userId) {
        return getEntityManager()
                .createQuery("select p from PriceList p " +
                        " inner join p.user.partners as part" +
                        " where part.partner.id = :userId " +
                        " and part.showPriceList = true " +
                        " and part.approved = true " +
                        " and part.confirmed = true")
                .setParameter("userId", userId)
                .getResultList();
    }
}
