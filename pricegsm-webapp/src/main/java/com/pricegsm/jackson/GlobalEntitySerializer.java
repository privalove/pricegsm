package com.pricegsm.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pricegsm.domain.GlobalEntity;
import com.pricegsm.util.ApplicationContextProvider;
import com.pricegsm.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import java.io.IOException;

/**
 * Serialize to short form of global entity {id:,name:}
 */
public class GlobalEntitySerializer
        extends JsonSerializer<GlobalEntity> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void serialize(GlobalEntity value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        try {
            jgen.writeObject(new GlobalEntityWrapper(value.getId(), Utils.convert(value, getConversionService())));
            return;
        } catch (UnsupportedOperationException uoe) {
            logger.warn("Unsupported operation for " + value, uoe);
        }

        // fallback
        jgen.writeObject(value);
    }

    private ConversionService getConversionService() {
        return ApplicationContextProvider.getApplicationContext().getBean(ConversionService.class);
    }
}
