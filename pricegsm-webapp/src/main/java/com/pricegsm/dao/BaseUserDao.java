package com.pricegsm.dao;

import com.pricegsm.domain.BaseUser;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class BaseUserDao
        extends GlobalEntityDao<BaseUser> {

    public BaseUser loadByEmail(String email) {
        try {
            return (BaseUser) getEntityManager()
                    .createQuery("select u from BaseUser u where u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
