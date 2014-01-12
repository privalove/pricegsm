(function () {
    'use strict';

    angular.module('pg.directives', [])
        .run(["$rootScope", "$templateCache", function ($rootScope, $templateCache) {
            /**
             * Safe Apply from https://coderwall.com/p/ngisma
             */
            $rootScope.safeApply = function (fn) {
                var phase = this.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && (typeof(fn) === 'function')) {
                        fn();
                    }
                } else {
                    this.$apply(fn);
                }
            };

            $rootScope.positive = function(value) {
                return value > 0;
            }

            $rootScope.negative = function(value) {
                return value < 0;
            }

        }])
        .directive('pgChart', function ($filter) {
            return {
                scope: {
                    pgChart: "=",
                    chartDetails: "="
                },
                link: function ($scope, element, ctrl) {
                    var chartContainer = element.find(".pg-chart-container");

                    $scope.$watch(function () {
                        return $filter("json")($scope.pgChart) + $filter("json")($scope.chartDetails);
                    }, function () {
                        $.plot(chartContainer, $scope.pgChart, $scope.chartDetails);
                    });
                }
            }
        })

    ;

})();