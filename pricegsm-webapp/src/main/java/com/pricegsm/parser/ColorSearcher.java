package com.pricegsm.parser;


import com.pricegsm.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:29
 */
public class ColorSearcher implements Searcher {

    private List<String> colors = new ArrayList<>();
    private List<String> excludedColors = new ArrayList<>();

    public ColorSearcher(String yandexColor, String excludedColorQuery) {
        if (!Utils.isEmpty(yandexColor)) {
            this.colors = Arrays.asList(yandexColor.split(","));
        }

        if (!Utils.isEmpty(excludedColorQuery)) {
            this.excludedColors = Arrays.asList(excludedColorQuery.split(","));
        }
    }

    @Override
    public boolean isCellFind(String data) {
        if (Utils.isEmpty(data)) {
            return false;
        }
        String dataLowerCase = data.toLowerCase();
        // exclude from search data excluded colors
        for (String excludedColor : excludedColors) {
            dataLowerCase = dataLowerCase.replace(excludedColor.toLowerCase(), "");
        }

        // searching appropriate color
        for (String color : colors) {
            if (dataLowerCase.matches("(^|^.*[\\W])" + color.toLowerCase() + "([\\W].*$|$)")) {
                return true;
            }
        }
        return false;
    }
}
