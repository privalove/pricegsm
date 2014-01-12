(function () {
    'use strict';

    angular.module('pricegsm', ['ngRoute', 'ngCookies', 'ngAnimate', 'ngGrid', 'ui.bootstrap', 'angular-loading-bar',
            'pg.services', 'pg.directives'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when('/', {templateUrl: R.base + '/index', controller: IndexCtrl, resolve: IndexCtrl.resolve})
                .when('/marketplace', {templateUrl: R.base + '/marketplace', controller: MarketplaceCtrl, resolve: MarketplaceCtrl.resolve})
                .when('/order', {templateUrl: R.base + '/order', controller: OrderCtrl, resolve: OrderCtrl.resolve})
                .when('/sales', {templateUrl: R.base + '/sales', controller: SalesCtrl, resolve: SalesCtrl.resolve})
                .when('/price_list', {templateUrl: R.base + '/price_list', controller: PriceListCtrl, resolve: PriceListCtrl.resolve})
                .when('/partner', {templateUrl: R.base + '/partner', controller: PartnerCtrl, resolve: PartnerCtrl.resolve})
                .when('/profile', {templateUrl: R.base + '/profile', controller: ProfileCtrl, resolve: ProfileCtrl.resolve})

                .otherwise({redirectTo: '/'});
        }])
})();