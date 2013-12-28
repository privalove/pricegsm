package com.pricegsm.service;

import com.pricegsm.domain.BaseEntity;
import com.pricegsm.domain.GlobalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Locate service by domain type.
 */
@Component
public class ServiceLocator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, BaseEntityService> entityServices = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * For given entity class name returns a corresponding data service that manages it
     *
     * @param type base entity to look service for
     * @return service for given domain object
     */
    public <T extends BaseEntity> BaseEntityService<T, ? extends Serializable> locateBaseService(Class<T> type) {
        initCache();
        return entityServices.get(type.getSimpleName());
    }

    /**
     * For given entity class name returns a corresponding data service that manages it
     *
     * @param type global entity to look service for
     * @return service for given domain object
     */
    public <T extends GlobalEntity> GlobalEntityService<T> locateService(Class<T> type) {
        initCache();
        return (GlobalEntityService<T>) entityServices.get(type.getSimpleName());
    }

    public synchronized void initCache() {
        if (entityServices.isEmpty()) {
            logger.info("Initializing service locator cache");
            for (BaseEntityService service : applicationContext.getBeansOfType(BaseEntityService.class).values()) {
                entityServices.put(service.getType().getSimpleName(), service);
            }
        }
    }
}
