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
public class GlobalEntityListSerializer
        extends JsonSerializer<GlobalEntityListWrapper> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void serialize(GlobalEntityListWrapper list, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        try {
            jgen.writeStartArray();
            for (GlobalEntity value : list.getList()) {
                jgen.writeObject(new GlobalEntityWrapper(value.getId(), Utils.convert(value, getConversionService())));
            }
            jgen.writeEndArray();
        } catch (UnsupportedOperationException uoe) {
            logger.warn("Unsupported operation for " + list, uoe);
        }
    }

    private ConversionService getConversionService() {
        return ApplicationContextProvider.getApplicationContext().getBean(ConversionService.class);
    }
}
