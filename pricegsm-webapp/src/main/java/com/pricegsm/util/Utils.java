package com.pricegsm.util;

import com.google.common.base.Charsets;
import com.google.common.primitives.Longs;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author andreybugaev
 * @since 15.12.2013
 */
public final class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

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

    /**
     * Midnight time 00:00
     */
    public static Date today() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 10:00, 14:00, 18:00
     */
    public static List<Date> yandexTimes() {
        return Arrays.asList(DateUtils.addHours(today(), 10), DateUtils.addHours(today(), 14), DateUtils.addHours(today(), 18));
    }

    /**
     * @return Closest yandex time from {@link #yandexTimes()}
     */
    public static Date yandexTime() {
        return yandexTime(null);
    }

    /**
     * @return Closest yandex time from {@link #yandexTimes()}
     */
    public static Date yandexTime(Date date) {
        if (date == null) {
            date = new Date();
        }

        final long time = date.getTime();
        List<Date> dates = new ArrayList<>(yandexTimes());
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {

                long t1 = Math.abs(o1.getTime() - time);
                long t2 = Math.abs(o2.getTime() - time);
                return Longs.compare(t1, t2);
            }
        });

        return dates.get(0);
    }

    public static String readFromUrl(String url) throws IOException {
        try(InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            return IOUtils.toString(rd);
        }
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try(InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            return new JSONObject(IOUtils.toString(rd));
        }
    }

    public static boolean isAccessible(PropertyDescriptor descriptor) {
        return descriptor.getWriteMethod() != null && descriptor.getReadMethod() != null;
    }

    /**
     * Removes braces {} in the begin and at the end of the validation message.
     */
    public static String adaptValidationMessage(String message) {
        if (!isEmpty(message) && message.startsWith("{") && message.endsWith("}")) {
            return message.substring(1, message.length() - 1);
        } else {
            return message;
        }
    }
}
