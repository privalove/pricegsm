(function () {
    'use strict';

    angular.module('pg.directives', [])
        .directive('pgChart', function ($filter) {
            return {
                scope: {
                    pgChart:"=",
                    chartDetails:"="
                },
                link: function($scope, element, ctrl) {
                    var chartContainer = element.find(".pg-chart-container");

                    $scope.$watch(function(){
                       return $filter("json")($scope.pgChart) + $filter("json")($scope.chartDetails);
                    }, function() {
                        $.plot(chartContainer, $scope.pgChart, $scope.chartDetails);
                    });
                }
            }
        })
    ;

})();