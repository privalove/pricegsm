(function () {
    'use strict';

    var RESOURCES = {
        "IndexResource" : "index.json",
        "IndexChartResource" : "index/chart.json",
        "IndexPriceResource" : "index/price.json",
        "IndexShopResource" : "index/shop.json",

        "Profile" : "profile.json",
        "ProfileMetadata" : "profile/metadata.json",
        "ProfileContext" : "profile/context.json",
        "ChangePassword" : "profile/changePassword",

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
            .factory('notifyManager', function($interpolate) {
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
            })
;


})();
