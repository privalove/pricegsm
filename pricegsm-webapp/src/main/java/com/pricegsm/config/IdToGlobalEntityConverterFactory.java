package com.pricegsm.config;

import com.pricegsm.domain.GlobalEntity;
import com.pricegsm.service.GlobalEntityService;
import com.pricegsm.service.ServiceLocator;
import com.pricegsm.util.ApplicationContextProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class IdToGlobalEntityConverterFactory
        implements ConverterFactory<Long, GlobalEntity> {

    @Override
    public <T extends GlobalEntity> Converter<Long, T> getConverter(Class<T> targetType) {

        return new IdToBaseEntityConverter<>(getServiceLocator().locateService(targetType));
    }

    private static class IdToBaseEntityConverter<E extends GlobalEntity>
            implements Converter<Long, E> {

        private GlobalEntityService<E> service;

        private IdToBaseEntityConverter(GlobalEntityService<E> service) {
            this.service = service;
        }

        @Override
        public E convert(Long source) {
            return service.load(source);
        }
    }

    public ServiceLocator getServiceLocator() {
        return ApplicationContextProvider.getApplicationContext().getBean(ServiceLocator.class);
    }

}
