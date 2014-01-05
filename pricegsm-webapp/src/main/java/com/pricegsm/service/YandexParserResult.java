package com.pricegsm.service;

import com.pricegsm.domain.YandexPrice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class YandexParserResult
        implements Serializable {

    private int error;

    private List<YandexPrice> offers = new ArrayList<>();

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<YandexPrice> getOffers() {
        return offers;
    }

    public void setOffers(List<YandexPrice> offers) {
        this.offers = offers;
    }
}
