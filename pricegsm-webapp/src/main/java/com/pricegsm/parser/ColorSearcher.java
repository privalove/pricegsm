package com.pricegsm.parser;


import java.util.Arrays;
import java.util.List;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:29
 */
public class ColorSearcher implements Searcher {

    private List<String> colors;

    public ColorSearcher(String yandexColor) {
        this.colors = Arrays.asList(yandexColor.split(","));
    }

    @Override
    public boolean isCellFind(String data) {
        String dataLowerCase = data.toLowerCase();
        for (String color : colors) {
            if (dataLowerCase.matches("(^|^.*[\\W])" + color.toLowerCase() + "([\\W].*$|$)")) {
                return true;
            }
        }
        return false;
    }
}
