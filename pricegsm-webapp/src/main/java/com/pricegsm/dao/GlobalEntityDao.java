package com.pricegsm.dao;

import com.pricegsm.domain.Activable;
import com.pricegsm.domain.GlobalEntity;

import javax.persistence.NoResultException;
import java.util.List;

public class GlobalEntityDao<E extends GlobalEntity>
        extends  BaseEntityDao<E, Long> {

    /**
     * @return Active entity by id or null, if entity doesn't exist or not active.
     * @throws UnsupportedOperationException if Entity is not {@link Activable}.
     */
    public E loadActive(long id) throws UnsupportedOperationException {
        if (Activable.class.isAssignableFrom(getType())) {
            try {
                return (E)getEntityManager()
                        .createQuery("select from " + getEntityName() + " o where o.id = :id and o.active = true " + getOrderBy("o"))
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } else {
            throw new UnsupportedOperationException(getEntityName() + " is not Activable");
        }
    }

    /**
     * @return All active entities.
     * @throws UnsupportedOperationException if Entity is not {@link Activable}
     */
    public List<E> findActive() throws UnsupportedOperationException {
        if (Activable.class.isAssignableFrom(getType())) {
            return getEntityManager()
                    .createQuery("select from " + getEntityName() + " o where o.active = true " + getOrderBy("o"))
                    .getResultList();
        } else {
            throw new UnsupportedOperationException(getEntityName() + " is not Activable");
        }
    }
}
