package com.pricegsm.parser;

import java.util.Arrays;
import java.util.List;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:28
 */
public class ProductNameSearcher implements Searcher {

    private List<String> nameFragments;

    public ProductNameSearcher(String name) {
        this.nameFragments = Arrays.asList(name.split(" "));
    }

    @Override
    public boolean isCellFind(String data) {
        String lowerCaseData = data.toLowerCase();
        for (String nameFragment : nameFragments) {
            if (!lowerCaseData.matches("(^|^.*[\\W])" + nameFragment.toLowerCase() + "([\\W].*$|$)")) {
                return false;
            }

        }
        return true;
    }
}
