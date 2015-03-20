package com.pricegsm.parser;

import com.pricegsm.domain.Product;

/**
 * User: o.logunov
 * Date: 20.03.15
 * Time: 23:56
 */
public enum PriceListDescriptor {

    MASTERFONE("masterfone", 1, 4, 3) {
        public String getSearchQuery(Product product) {
            return product.getSearchPriceListQuery();
        }
    },
    DUNAMIS("dunamis", 2, 4, 6) {
        public String getSearchQuery(Product product) {
            return product.getDunamisQuery();
        }
    };

    private String name;
    private int productNameIndex;
    private int priceColumnIndex;
    private int descriptionColumnIndex;

    private PriceListDescriptor(
            String name, int productNameIndex, int priceColumnIndex, int descriptionColumnIndex) {
        this.name = name;
        this.productNameIndex = productNameIndex;
        this.priceColumnIndex = priceColumnIndex;
        this.descriptionColumnIndex = descriptionColumnIndex;
    }

    public static PriceListDescriptor getDescriptor(String sellerName) {
        if (sellerName.equals(MASTERFONE.getName())) {
            return MASTERFONE;
        } else {
            return DUNAMIS;
        }
    }

    public abstract String getSearchQuery(Product product);

    public String getName() {
        return name;
    }

    public int getProductNameIndex() {
        return productNameIndex;
    }

    public int getPriceColumnIndex() {
        return priceColumnIndex;
    }

    public int getDescriptionColumnIndex() {
        return descriptionColumnIndex;
    }
}