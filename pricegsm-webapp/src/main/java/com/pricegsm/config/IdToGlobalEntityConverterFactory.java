package com.pricegsm.config;

import com.pricegsm.domain.GlobalEntity;
import com.pricegsm.service.GlobalEntityService;
import com.pricegsm.service.ServiceLocator;
import com.pricegsm.util.ApplicationContextProvider;
import com.pricegsm.util.Utils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;

public class IdToGlobalEntityConverterFactory
        implements ConverterFactory<String, GlobalEntity> {

    private final ConversionService registry;

    public IdToGlobalEntityConverterFactory(ConversionService registry) {
        this.registry = registry;
    }

    @Override
    public <T extends GlobalEntity> Converter<String, T> getConverter(Class<T> targetType) {

        return new IdToBaseEntityConverter<>(getServiceLocator().locateService(targetType), registry);
    }

    private static class IdToBaseEntityConverter<E extends GlobalEntity>
            implements Converter<String, E> {

        private GlobalEntityService<E> service;

        private ConversionService registry;

        private IdToBaseEntityConverter(GlobalEntityService<E> service, ConversionService registry) {
            this.service = service;
            this.registry = registry;
        }

        @Override
        public E convert(String source) {
            return service.load(registry.convert(source, Long.class));
        }
    }

    public ServiceLocator getServiceLocator() {
        return ApplicationContextProvider.getApplicationContext().getBean(ServiceLocator.class);
    }

}
