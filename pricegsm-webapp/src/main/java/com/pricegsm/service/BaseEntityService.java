package com.pricegsm.service;

import com.pricegsm.dao.BaseEntityDao;
import com.pricegsm.domain.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class BaseEntityService<E extends BaseEntity, K extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ApplicationContext context;

    protected abstract BaseEntityDao<E, K> getDao();

    /**
     * @return Primary Key of entity.
     */
    protected abstract K getPK(E entity);

    public E getDefaultInstance() {
        try {
            return this.getType().newInstance();
        } catch (Exception e) {
            //w not happen
            logger.error("Impossible to instantiate entity {}", getDao().getEntityName());
            throw new RuntimeException(e);
        }
    }


    public List<E> findAll() {
        List<E> list = new ArrayList<>(getDao().findAll());
        return postLoad(list);
    }

    public List<E> findByModified(Date modified) {
        List<E> list = new ArrayList<>(getDao().findByModified(modified));
        return postLoad(list);
    }

    public E load(K pk) {
        return postLoad(getDao().load(pk));
    }

    @Transactional
    public SaveResult<E> save(E entity) {
        entity = preSave(entity);
        SaveResult<E> result = innerSave(entity, getPK(entity));
        postSave(entity, result);

        return result;
    }

    @Transactional
    public void saveAll(Collection<E> list) {
        for (E entity : list) {
            save(entity);
        }
        getDao().flush();
    }

    @Transactional
    public DeleteResult delete(K id) {
        E entity = getDao().load(id);
        DeleteResult result = DeleteResult.OK;
        if (entity != null) {
            entity = preDelete(entity);
            if (entity != null) {
                getDao().delete(entity);
            } else {
                result = DeleteResult.REFUSED;
            }
        } else {
            result = DeleteResult.NOT_EXISTS;
        }

        postDelete(id, entity, result);

        return result;
    }

    /**
     * Inner method which decides persist or merge method should be called.
     *
     * @param entity Entity.
     * @param pk     Primary Key.
     * @return true - if persisted, false - if merged.
     */
    protected SaveResult<E> innerSave(E entity, K pk) {
        E persisted = getDao().load(pk);
        boolean isSave = true;
        if (persisted != null) {
            merge(persisted, entity);
            isSave = false;
        }
        getDao().saveOrUpdate(entity);
        return new SaveResult<>(entity, isSave);
    }

    protected void postDelete(K id, E entity, DeleteResult result) {
    }

    protected E preDelete(E entity) {
        return entity;
    }

    /**
     * Merges entity from web form with persisted entity before saved to database.
     *
     * @param persisted Entity is stored in database.
     * @param entity    Entity from web form.
     */
    protected void merge(E persisted, E entity) {

    }


    protected void postSave(E entity, SaveResult<E> result) {
    }

    protected E preSave(E entity) {
        return entity;
    }


    protected E postLoad(E entity) {
        return entity;
    }

    protected List<E> postLoad(List<E> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, postLoad(list.get(i)));

        }
        return list;
    }

    public Class<E> getType() {
        return getDao().getType();
    }

    public Class<K> getKeyType() {
        return getDao().getKeyType();
    }

    public void clear() {
        getDao().clearAll();
    }

    public void flush() {
        getDao().flush();
    }

    public String getEntityName() {
        return getDao().getEntityName();
    }
}
