package com.pricegsm.parser;

import com.pricegsm.domain.Product;
import com.pricegsm.domain.WorldPrice;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: o.logunov
 * Date: 13.02.15
 * Time: 0:41
 */
public abstract class PriceListParser<T> {

    public List<WorldPrice> parse(T source, List<Product> products) {

        ArrayList<WorldPrice> prices = new ArrayList<>();
        try {
            openSource(source);

            for (Product product : products) {
                List<WorldPrice> results = getResults(product);

                if (!results.isEmpty()) {
                    prices.addAll(results);
                }
                resetSource();
            }
            closeSource(source);

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return prices;
    }

    protected abstract void openSource(T source) throws IOException, InvalidFormatException;

    protected abstract void resetSource();

    protected abstract void closeSource(T source) throws IOException;

    private List<WorldPrice> getResults(Product product) {
        List<Searcher> searchers = new ArrayList<>();
        searchers.add(new ProductNameSearcher(product.getSearchPriceListQuery()));
        searchers.add(new ColorSearcher(product.getColorQuery()));

        List<List<String>> rows = findRows(searchers);

        ArrayList<WorldPrice> worldPrices = new ArrayList<>();
        int position = 1;
        for (List<String> row : rows) {
            if (row.isEmpty()) {
                continue;
            }

            WorldPrice price = new WorldPrice();
            price.setProduct(product);
            price.setPriceUsd(getPrice(row));
            price.setDescription(row.get(3));
            price.setPosition(position);
            position++;
            worldPrices.add(price);
        }

        return worldPrices;
    }

    private List<List<String>> findRows(List<Searcher> searchers) {
        List<List<String>> rows = new ArrayList<>();
        while (hasNextRow()) {
            List<String> row = getNextRow();
            if (isNeededRow(row, searchers)) {
                rows.add(row);
            }
        }
        return rows;
    }

    protected abstract boolean hasNextRow();

    protected abstract List<String> getNextRow();

    private boolean isNeededRow(List<String> row, List<Searcher> searchers) {
        Iterator<String> cellIterator = row.iterator();
        int findCounter = 0;
        while (cellIterator.hasNext()) {
            String cell = cellIterator.next();
            List<Searcher> excludedSearchers = new ArrayList<>();
            for (Searcher searcher : searchers) {
                if (!excludedSearchers.contains(searcher)
                        && searcher.isCellFind(cell)) {
                    findCounter++;
                    excludedSearchers.add(searcher);
                    break;
                }
            }
            if (findCounter == searchers.size()) {
                return true;
            }
        }
        return false;
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
}
