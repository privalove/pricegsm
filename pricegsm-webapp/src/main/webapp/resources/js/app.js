(function () {
    'use strict';

    angular.module('pricegsm', ['ngRoute', 'ngCookies', 'ngAnimate', 'ngGrid', 'ui.bootstrap', 'ui.unique', 'angular-loading-bar', 'toggle-switch',
            'pg.services', 'pg.directives','orderFilters'])
        .config(['$routeProvider', "$httpProvider", function ($routeProvider, $httpProvider) {
            $routeProvider
                .when('/', {templateUrl: R.base + 'index', controller: IndexCtrl, resolve: IndexCtrl.resolve, title: 'page.main.title' })
                .when('/marketplace', {templateUrl: R.base + 'marketplace', controller: MarketplaceCtrl, resolve: MarketplaceCtrl.resolve, title: 'page.marketplace.title'})
                .when('/order', {templateUrl: R.base + 'order', controller: OrderCtrl, resolve: OrderCtrl.resolve, title: 'page.order.title'})
                .when('/sales', {templateUrl: R.base + 'sales', controller: SalesCtrl, resolve: SalesCtrl.resolve, title: 'page.sales.title'})
                .when('/price_list', {templateUrl: R.base + 'price_list', controller: PriceListCtrl, resolve: PriceListCtrl.resolve, title: 'page.priceList.title'})
                .when('/partner', {templateUrl: R.base + 'partner', controller: PartnerCtrl, resolve: PartnerCtrl.resolve, title: 'page.partner.title'})
                .when('/profile', {templateUrl: R.base + 'profile', controller: ProfileCtrl, resolve: ProfileCtrl.resolve, title: 'page.profile.title'})

                .otherwise({redirectTo: '/'});

            //redirect on ajax response error
            $httpProvider.responseInterceptors.push(["$q", function($q) {

                function success(response) {
                    return response;
                }

                function error(response) {
                    var status = response.status;

                    if (status == 401) {
                        window.location = R.base;
                        return;
                    }

                    // otherwise
                    return $q.reject(response);

                }

                return function (promise) {
                    return promise.then(success, error);
                }

            }]);


        }])
        .run(["$rootScope", "$route", "datepickerConfig", "datepickerPopupConfig", function($rootScope, $route, datepickerConfig, datepickerPopupConfig) {
            $rootScope.$on("$routeChangeSuccess", function(){
                //Change page title, based on Route information
                $rootScope.title = R.get($route.current.title);
            });

            $rootScope.contains = function(value, array) {
                return $.inArray(value, array) >= 0;
            };

            datepickerConfig.startingDay = 1;
            datepickerPopupConfig.showButtonBar = false;
            datepickerPopupConfig.dateFormat = "dd.MM.yy";
        }])
})();