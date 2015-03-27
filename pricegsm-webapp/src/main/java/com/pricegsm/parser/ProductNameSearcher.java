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

        searchQueries = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            List<String> searchQueryStrings = Arrays.asList(name.split(","));
            for (String searchQuery : searchQueryStrings) {
                searchQueries.add(Arrays.asList(searchQuery.split(" ")));
            }
        }
    }

    @Override
    public boolean isCellFind(String data) {
        if (searchQueries.isEmpty() || data.isEmpty()) {
            return false;
        }
        String lowerCaseData = data.toLowerCase();

        for (List<String> nameFragments : searchQueries) {
            boolean error = false;
            int findCounter = 0;
            for (String nameFragment : nameFragments) {
                if (!lowerCaseData.matches("(^|^.*[\\W])"
                        + nameFragment.toLowerCase()
                        + "(gB[\\W].*$|Gb[\\W].*$|GB[\\W].*$|gb[\\W].*$|[\\W].*$|"
                        + "GB$|gb$|gB$|Gb$|$)")) {
                    error = true;
                    break;
                }
                findCounter++;
            }
            if (!error && findCounter == lowerCaseData.split(" ").length) {
                return true;
            }
        }
        return false;
    }
}
