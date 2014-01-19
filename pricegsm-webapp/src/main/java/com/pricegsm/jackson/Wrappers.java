package com.pricegsm.jackson;

import com.pricegsm.domain.GlobalEntity;
import com.pricegsm.util.ApplicationContextProvider;
import com.pricegsm.util.Utils;
import org.springframework.core.convert.ConversionService;

import java.util.Date;
import java.util.List;

/**
 * Wrapper for JSON serializers
 */
public final class Wrappers {

    private Wrappers() {
    }

    /**
     * @return [{id:ID, name:NAME},{id:ID, name:NAME}]
     */
    public static GlobalEntityListWrapper wrap(List<? extends GlobalEntity> list) {
        return new GlobalEntityListWrapper(list);
    }

    /**
     * @return {id:ID, name:NAME}
     */
    public static GlobalEntityWrapper wrap(GlobalEntity value) {
        return new GlobalEntityWrapper(value.getId(), Utils.convert(value, getConversionService()));
    }

    /**
     * @return yyyy-MM-dd
     */
    public static DateWrapper wrapDate(Date date) {
        return new DateWrapper(date);
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static TimestampWrapper wrapTimestamp(Date date) {
        return new TimestampWrapper(date);
    }

    private static ConversionService getConversionService() {
        return ApplicationContextProvider.getApplicationContext().getBean(ConversionService.class);
    }


}
