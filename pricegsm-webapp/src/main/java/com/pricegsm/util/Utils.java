package com.pricegsm.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.util.Collection;

/**
 * @author andreybugaev
 * @since 15.12.2013
 */
public final class Utils {

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
}
