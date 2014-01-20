package com.pricegsm.parser;

import java.util.ArrayList;
import java.util.List;

public class YandexMarketResult {

    private Result result = new Result();

    public Result getResult() {
        return result;
    }

    public static class Result {
        private List<Position> offers = new ArrayList<>();

        private int error = 0;

        public List<Position> getOffers() {
            return offers;
        }

        public int getError() {
            return error;
        }
    }

}
