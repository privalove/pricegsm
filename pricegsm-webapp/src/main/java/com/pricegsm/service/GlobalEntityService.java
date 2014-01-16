package com.pricegsm.service;

import com.pricegsm.dao.GlobalEntityDao;
import com.pricegsm.domain.Color;
import com.pricegsm.domain.GlobalEntity;

import java.util.List;

public abstract class GlobalEntityService<E extends GlobalEntity>
        extends BaseEntityService<E, Long> {

    @Override
    protected abstract GlobalEntityDao<E> getDao();

    @Override
    protected Long getPK(E entity) {
        return entity.getId();
    }
}
