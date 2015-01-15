package com.pricegsm.dao;

import com.pricegsm.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDao
        extends GlobalEntityDao<User> {

    public User loadByEmail(String email) {
        try {
            return (User) getEntityManager()
                    .createQuery("select u from User u where lower(u.email) = lower(:email)")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User loadByOrganizationName(String organizationName) {
        try {
            return (User) getEntityManager()
                    .createQuery("select u from User u where u.organizationName = :organizationName")
                    .setParameter("organizationName", organizationName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
