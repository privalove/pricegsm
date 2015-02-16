package com.pricegsm.parser;

import com.pricegsm.domain.Product;
import com.pricegsm.domain.WorldPrice;
import com.pricegsm.domain.YandexPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: o.logunov
 * Date: 13.02.15
 * Time: 0:41
 */
public abstract class PriceListParser<T> {


    public List<WorldPrice> parse(T source, List<Product> products) {

        ArrayList<WorldPrice> prices = new ArrayList<>();
        for (Product product : products) {
            WorldPrice result = getResult(source, product);

            if (result != null) {
                prices.add(result);
            }
        }
        return prices;
    }

    private WorldPrice getResult(T source, Product product) {
        List<Searcher> searchers = new ArrayList<>();
        searchers.add(new ProductNameSearcher(product.getSearchQuery()));
        searchers.add(new ColorSearcher(product.getColorQuery()));

        List<String> row = findRow(source, searchers);

        if (row.isEmpty()) {
            return null;
        }

        WorldPrice price = new WorldPrice();
        price.setProduct(product);
        price.setPriceUsd(getPrice(row));

        return price;
    }
    //todo add exeption
    private BigDecimal getPrice(List<String> row) {
        BigDecimal price = null;
        PriceSearcher priceSearcher = new PriceSearcher();
        for (String cell : row) {
            if (priceSearcher.isCellFind(cell)) {
//               todo
               price = new BigDecimal(cell);
            }

        }
        return price;
    }

    protected abstract List<String> findRow(T source, List<Searcher> searchers);
}
