package com.pricegsm.domain;

import java.io.Serializable;

public class AppSettings
        implements Serializable {

    public static String getParserUrl() {
        return "http://localhost:8080/parser";
    }

    public static long getPrimeProduct() {
        return 1L;
    }
}
