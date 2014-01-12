(function () {
    'use strict';

    angular.module('pricegsm', ['ngRoute', 'ngCookies', 'ngAnimate', 'ngGrid', 'ui.bootstrap', 'angular-loading-bar',
            'pg.services', 'pg.directives'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when('/', {templateUrl: R.base + 'index', controller: IndexCtrl, resolve: IndexCtrl.resolve, title: 'page.main.title' })
                .when('/marketplace', {templateUrl: R.base + 'marketplace', controller: MarketplaceCtrl, resolve: MarketplaceCtrl.resolve, title: 'page.marketplace.title'})
                .when('/order', {templateUrl: R.base + 'order', controller: OrderCtrl, resolve: OrderCtrl.resolve, title: 'page.order.title'})
                .when('/sales', {templateUrl: R.base + 'sales', controller: SalesCtrl, resolve: SalesCtrl.resolve, title: 'page.sales.title'})
                .when('/price_list', {templateUrl: R.base + 'price_list', controller: PriceListCtrl, resolve: PriceListCtrl.resolve, title: 'page.priceList.title'})
                .when('/partner', {templateUrl: R.base + 'partner', controller: PartnerCtrl, resolve: PartnerCtrl.resolve, title: 'page.partner.title'})
                .when('/profile', {templateUrl: R.base + 'profile', controller: ProfileCtrl, resolve: ProfileCtrl.resolve, title: 'page.profile.title'})

                .otherwise({redirectTo: '/'});
        }])
        .run(["$rootScope", "$route", function($rootScope, $route) {
            $rootScope.$on("$routeChangeSuccess", function(){
                //Change page title, based on Route information
                $rootScope.title = R.get($route.current.title);
            });
        }
        ])
})();