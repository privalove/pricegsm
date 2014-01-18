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
import java.text.SimpleDateFormat;

public class DateWrapperSerializer
        extends JsonSerializer<DateWrapper> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void serialize(DateWrapper value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        try {

            if (value.getDate() != null) {
                jgen.writeString(new SimpleDateFormat("yyyy-MM-dd").format(value.getDate()));
            } else {
                jgen.writeNull();
            }

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
