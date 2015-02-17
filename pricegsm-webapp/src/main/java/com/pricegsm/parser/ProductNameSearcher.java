package com.pricegsm.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:28
 */
public class ProductNameSearcher implements Searcher {

    private final ArrayList<List<String>> searchQueries;

    public ProductNameSearcher(String name) {

        List<String> searchQueryStrings = Arrays.asList(name.split(","));
        searchQueries = new ArrayList<>();
        for (String searchQuery : searchQueryStrings) {
            searchQueries.add(Arrays.asList(searchQuery.split(" ")));
        }
    }

    @Override
    public boolean isCellFind(String data) {
        String lowerCaseData = data.toLowerCase();
        boolean error = false;
        for (List<String> nameFragments : searchQueries) {
            for (String nameFragment : nameFragments) {
                if (!lowerCaseData.matches("(^|^.*[\\W])"
                        + nameFragment.toLowerCase()
                        + "(gB[\\W].*$|Gb[\\W].*$|GB[\\W].*$|gb[\\W].*$|[\\W].*$|"
                        + "GB$|gb$|gB$|Gb$|$)")) {
                    error = true;
                    break;
                }
            }
            if (!error) {
                return true;
            }
        }
        return false;
    }
}
