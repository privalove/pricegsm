(function () {
    'use strict';

    var RESOURCES = {
        "IndexResource" : "index.json",
        "IndexChartResource" : "index/chart.json",
        "IndexPriceResource" : "index/price.json",
        "IndexShopResource" : "index/shop.json",

        "PriceListResource": "price_list.json"
    };

        angular.module('pg.services', ['ngResource'])
            .config(function ($provide) {
                angular.forEach(RESOURCES, function (jsonLocation, name) {
                    $provide.factory(name, function ($resource) {
                        return $resource(R.base + jsonLocation);
                    })
                });
            })


})();
