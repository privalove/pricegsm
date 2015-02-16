package com.pricegsm.parser;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:31
 */
public class PriceSearcher implements Searcher {

    @Override
    public boolean isCellFind(String data) {
        try {
            Double.parseDouble(data.replaceAll(",", "."));
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
