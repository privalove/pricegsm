package com.pricegsm.config;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Properties;

public interface PricegsmMessageSource extends MessageSource {
    Properties getProperties(Locale locale);
}
