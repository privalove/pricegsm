package com.pricegsm.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.util.Collection;

/**
 * @author andreybugaev
 * @since 15.12.2013
 */
public final class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static boolean isEmpty(String string) {
        return StringUtils.isBlank(string);
    }

    public static boolean isEmpty(Collection collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /**
     * convert HelloWorld to hello_world
     *
     * @param str String to convert.
     * @return Result string.
     */
    public static String camelCaseToUnderline(String str) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(str);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append("_");
            }
            result.append(StringUtils.uncapitalize(words[i]));
        }

        return result.toString();
    }

    /**
     * convert hello_world to HelloWorld
     *
     * @param str String to convert.
     * @return Result string.
     */
    public static String underlineToCamelCase(String str) {
        String[] words = str.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(StringUtils.capitalize(word));
        }

        return result.toString();
    }

    public static String md5(String stringToCheck) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(stringToCheck.getBytes("UTF-8"));

            StringBuilder hashAsHexString = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                hashAsHexString.append(String.format("%02x", b));
            }

            return hashAsHexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert base entity to label for UI identifier.
     * <p/>
     * First It looks for label in conversion service.
     * If converter has not been found it looks for 'name' property.
     * If there is no 'name' property in entity it returns {@link #toString()} at last.
     *
     * @param entity  Base entity.
     * @param factory Conversion factory.
     * @return Label.
     */
    public static String convert(Object entity, ConversionService factory) {
        if (entity == null) {
            return "";
        }

        if (factory.canConvert(entity.getClass(), String.class)) {
            return factory.convert(entity, String.class);
        }
        if (PropertyUtils.isReadable(entity, "name")) {
            try {
                return String.valueOf(PropertyUtils.getProperty(entity, "name"));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return String.valueOf(entity);
    }

}
