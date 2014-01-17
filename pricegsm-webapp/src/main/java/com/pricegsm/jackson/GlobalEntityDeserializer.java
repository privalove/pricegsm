package com.pricegsm.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.pricegsm.domain.GlobalEntity;
import com.pricegsm.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import java.io.IOException;

/**
 * Deserialize short form of global entity {id:,name:}.
 */
public class GlobalEntityDeserializer
        extends JsonDeserializer<GlobalEntity>
        implements ContextualDeserializer {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private Class type;

    public GlobalEntityDeserializer() {
    }

    public GlobalEntityDeserializer(Class type) {
        this.type = type;
    }

    public Class getValueClass() {
        return type;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return new GlobalEntityDeserializer(property.getType().getRawClass());
    }

    @Override
    public GlobalEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        GlobalEntityWrapper wrapper = jp.readValueAs(GlobalEntityWrapper.class);

        if (wrapper == null) {
            return null;
        }

        ConversionService conversionService = getConversionService();
        if (conversionService.canConvert(String.class, getValueClass())) {
            return (GlobalEntity) conversionService.convert(String.valueOf(wrapper.getId()), getValueClass());
        }

        logger.error("Type " + getValueClass().getSimpleName() + " is not supported by conversion service");

        return null;
    }

    private ConversionService getConversionService() {
        return ApplicationContextProvider.getApplicationContext().getBean(ConversionService.class);
    }
}
