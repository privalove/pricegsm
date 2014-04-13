(function () {
    'use strict';

    var RESOURCES = {
        "IndexResource" : "index.json",
        "IndexChartResource" : "index/chart.json",
        "IndexShopResource" : "index/shop.json",

        "Profile" : "profile.json",
        "ProfileContext" : "profile/context.json",
        "ChangePassword" : "profile/changePassword",

        "PriceListResource": "price_list.json",
        "PriceList": "price_list/:priceListId.json",
        "WorkConditions": "price_list/work_conditions.json",

        "PriceLists": "marketplace/pricelists.json",
        "Orders": "marketplace/orders.json",
        "Order": "marketplace/order.json"
    };

        angular.module('pg.services', ['ngResource'])
            .config(["$provide", function ($provide) {
                angular.forEach(RESOURCES, function (jsonLocation, name) {
                    $provide.factory(name, ["$resource", function ($resource) {
                        return $resource(R.base + jsonLocation);
                    }])
                });
            }])
            .factory('notifyManager', [function() {
                var settings = {
                    styling: 'bootstrap',
                    type: 'success',
                    animation: 'none',
                    maxonscreen: 2,
                    history: false,
                    delay: 3000
                };

                return {
                    success: function(message) {
                        $.pnotify(angular.extend({}, settings, {
                            text: message
                        }));
                    },
                    info: function(message) {
                        $.pnotify(angular.extend({}, settings, {
                            text: message,
                            type: 'info'
                        }));
                    },
                    error: function(message) {
                        $.pnotify(angular.extend({}, settings, {
                            text: message,
                            type: 'error'
                        }));
                    }

                };
            }])
;


})();
