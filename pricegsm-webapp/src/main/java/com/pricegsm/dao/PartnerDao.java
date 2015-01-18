package com.pricegsm.dao;

import com.pricegsm.domain.Partner;
import com.pricegsm.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class PartnerDao
        extends GlobalEntityDao<Partner> {
    public Partner getPartnerByUser(long currentUserId, long userId) {
        return (Partner) getEntityManager()
                .createQuery("select p from Partner p where p.user.id = :currentUserId and p.partner.id = :userId")
                .setParameter("currentUserId", currentUserId)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
