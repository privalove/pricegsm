(function () {
    'use strict';

    var RESOURCES = {
        "IndexResource" : "/index.json"
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
