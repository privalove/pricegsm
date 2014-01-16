package com.pricegsm.dao;

import com.pricegsm.domain.BaseEntity;
import com.pricegsm.util.ReflectionUtils;
import com.sun.tools.internal.jxc.gen.config.Classes;
import org.hibernate.jpa.HibernateEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class BaseEntityDao<E extends BaseEntity, K extends Serializable> {

    private static final List<String> ORDER_BY_NAME = Arrays.asList("name");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private HibernateEntityManager entityManager;

    private Class<E> type;

    private Class<K> keyType;

    @PostConstruct
    public void init() {
        this.type = ReflectionUtils.getGenericParameterClass(getClass(), BaseEntityDao.class, 0);
        this.keyType = ReflectionUtils.getGenericParameterClass(getClass(), BaseEntityDao.class, 1);
    }

    /**
     * Class of entity.
     */
    public Class<E> getType() {
        return type;
    }

    /**
     * Class of primary key.
     */
    public Class<K> getKeyType() {
        return keyType;
    }

    protected HibernateEntityManager getEntityManager() {
        return entityManager;
    }

    protected Logger getLogger() {
        return logger;
    }

    protected Iterable<String> getOrderByProperties() {
        return ORDER_BY_NAME;
    }

    /**
     * Returns JPA entity name
     *
     * @return Mapped entity name.
     */
    public String getEntityName() {
        return getType().getSimpleName();
    }

    public E load(K pk) {
        return getEntityManager().find(getType(), pk);
    }

    public void delete(E entity) {
        getEntityManager().remove(entity);
    }

    public void saveOrUpdate(E entity) {
        getEntityManager().getSession().saveOrUpdate(entity);
    }

    public void persist(E entity) {
        getEntityManager().persist(entity);
    }

    public E merge(E entity) {
        return getEntityManager().merge(entity);
    }

    public List<E> findAll() {
        StringBuilder builder = new StringBuilder();
        builder.append("select o from ")
                .append(getEntityName()).append(" o ")
                .append(getOrderBy("o"));

        Query query = getEntityManager().createQuery(builder.toString());

        return query.getResultList();
    }

    public E findFirst() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("select o from ")
                .append(getEntityName()).append(" o ")
                .append(getOrderBy("o"));
        try {
            return (E) getEntityManager().createQuery(builder.toString()).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<E> findByModified(Date modified) {
        return getEntityManager().createQuery("select o from " + getEntityName() + " o where o.modified >= :modified" + getOrderBy("o"))
                .setParameter("modified", modified)
                .getResultList();
    }


    protected String getOrderBy(String alias) {
        alias = normalizeAlias(alias);
        if (hasProperties(getOrderByProperties())) {
            StringBuilder builder = new StringBuilder(" order by");
            int index = 0;
            for (String property : getOrderByProperties()) {
                builder.append(" ");
                if (index > 0) {
                    builder.append(",");
                }
                builder.append(alias).append(property);
                index++;
            }
            return builder.toString();
        }
        return "";
    }

    protected String normalizeAlias(String alias) {
        if (alias == null) {
            alias = "";
        } else if (alias.length() > 0 && alias.lastIndexOf(".") < 0) {
            alias = alias + ".";
        }
        return alias;
    }

    protected boolean hasProperties(Iterable<String> names) {
        EntityType<E> type = getEntityManager().getMetamodel().entity(getType());
        Attribute atr;
        for (String name : names) {
            try {
                atr = type.getAttribute(name);
            } catch (Exception e) {
                return false;
            }
            if (atr == null) {
                return false;
            }
        }
        return true;
    }


    public void detach(E entity) {
        getEntityManager().detach(entity);
    }

    public void detachAll(List<E> entities) {
        for (E entity : entities) {
            getEntityManager().detach(entity);
        }
    }

    /**
     * Clear JPA cache.
     *
     * @see javax.persistence.EntityManager#clear()
     * @see javax.persistence.Cache#evictAll()
     */
    public void clearAll() {
        getEntityManager().clear();
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    /**
     * Remove from the JPA cache all entities of current type.
     */
    public void clear() {
        getEntityManager().getEntityManagerFactory().getCache().evict(getType());
    }

    /**
     * Remove from the JPA cache entity of current type by PK.
     *
     * @param key PK.
     */
    public void clear(K key) {
        getEntityManager().getEntityManagerFactory().getCache().evict(getType(), key);
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void refresh(E entity) {
        getEntityManager().refresh(entity);
    }

    public boolean isAttached(Object entity) {
        return getEntityManager().contains(entity);
    }
}
