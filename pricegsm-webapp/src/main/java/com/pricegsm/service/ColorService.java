package com.pricegsm.service;

import com.pricegsm.dao.ColorDao;
import com.pricegsm.domain.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService
        extends GlobalEntityService<Color> {

    @Autowired
    private ColorDao dao;

    @Override
    protected ColorDao getDao() {
        return dao;
    }

    public List<Color> findActive() {
        return getDao().findActive();
    }
}
