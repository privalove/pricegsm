package com.pricegsm.service;

import com.pricegsm.dao.GlobalEntityDao;
import com.pricegsm.domain.GlobalEntity;

public abstract class GlobalEntityService<E extends GlobalEntity>
        extends BaseEntityService<E, Long> {

    @Override
    protected abstract GlobalEntityDao<E> getDao();

    @Override
    protected Long getPK(E entity) {
        return entity.getId();
    }
}
