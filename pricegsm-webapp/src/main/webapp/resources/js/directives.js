(function () {
    'use strict';

    angular.module('pg.directives', [])
        .run(["$rootScope", function ($rootScope) {
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
        }])
        .directive('pgChart', ["$filter", function ($filter) {
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
                        if ($scope.pgChart != undefined && $scope.chartDetails != undefined) {
                            $.plot(chartContainer, $scope.pgChart, $scope.chartDetails);
                        }
                    });
                }
            }
        }])
    /**
     * Set text of element by eval content of directive.
     *
     * <button ng-text="R.get('button.close') + ' ' + R.get('restaurant')" >Close Restaurant</button>
     */
        .directive('ngText', [function () {
            return {
                restrict: "A",
                link: function ($scope, element, attrs) {
                    element.html($scope.$eval(attrs.ngText));
                }
            };
        }])
    /**
     * Set text of element by i18n key.
     *
     * <button pg-text="button.close">Close</button>
     */
        .directive('pgText', [function () {
            return {
                restrict: "A",
                link: function ($scope, element, attrs) {
                    element.html(R.get(attrs.pgText));
                }
            };
        }])
    /**
     * pg-valid="entityMetadata"
     * data-valid-options={form:'.form-group'}
     */
        .directive('pgValid', [function () {

            function isUndefined(value) {
                return typeof value == 'undefined';
            }

            function isEmpty(value) {
                return isUndefined(value) || value === '' || value === null || value !== value
                    || ($.isArray(value) && value.length <= 0);
            }

            var standardValidations = {
                pattern: function (ctrl, regexp) {
                    return function (value) {
                        if (isEmpty(value) || new RegExp(regexp).test(value)) {
                            ctrl.$setValidity('pattern', true);
                            return value;
                        } else {
                            ctrl.$setValidity('pattern', false);
                            return undefined;
                        }
                    }
                },
                minlength: function (ctrl, minlength) {
                    return function (value) {
                        if (!isEmpty(value) && value.length < minlength) {
                            ctrl.$setValidity('minlength', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('minlength', true);
                            return value;
                        }
                    }
                },
                maxlength: function (ctrl, maxlength) {
                    return function (value) {
                        if (!isEmpty(value) && value.length > maxlength) {
                            ctrl.$setValidity('maxlength', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('maxlength', true);
                            return value;
                        }
                    }
                },
                min: function (ctrl, min) {
                    return function (value) {
                        if (!isEmpty(value) && value < min) {
                            ctrl.$setValidity('min', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('min', true);
                            return value;
                        }
                    }
                },
                max: function (ctrl, max) {
                    return function (value) {
                        if (!isEmpty(value) && value > max) {
                            ctrl.$setValidity('max', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('max', true);
                            return value;
                        }
                    }
                },
                required: function (ctrl) {
                    return function (value) {
                        if (isEmpty(value)) {
                            ctrl.$setValidity('required', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('required', true);
                            return value;
                        }
                    }
                }
            };


            var config = {
                form: '.form-group'
            };


            return {
                restrict: 'A',
                require: "ngModel",
                link: function (scope, iElement, attrs, controller) {
                    var opts = angular.extend(config, scope.$eval(attrs.validOptions) || {});
                    var subForm = iElement.parents(opts.form);
                    var name = iElement.attr("name");
                    var metadata = scope.$eval(attrs.pgValid);
                    var validations = metadata.columns[name].validations;

                    angular.forEach(validations, function (validation, index) {
                        angular.forEach(validation.keys, function (value, key) {
                            controller.$parsers.push(standardValidations[key](controller, value));
                            controller.$formatters.push(standardValidations[key](controller, value));
                        });

                        var elId = name + validation.name + "Error";
                        var el = iElement.find("#" + elId);
                        var messageValue = R.get(validation.message, validation.args);
                        if (el.length == 0) {
                            el = $("<small id='" + elId + "' class='help-block pg-error-message'></small>");
                            iElement.after(el);


                        }
                        el.html("<i class='fa fa-times-circle fa-lg'></i>" + messageValue);

                        var isValid = function (controller, keys) {
                            var error = false;

                            angular.forEach(keys, function (value, key) {
                                error = error || controller.$error[key];
                            });

                            return !error;
                        };

                        scope.$watch(function () {
                            return (controller.$submitted || controller.$dirty) && !isValid(controller, validation.keys);
                        }, function () {
                            if ((controller.$submitted || controller.$dirty) && !isValid(controller, validation.keys)) {
                                el.show();
                            } else {
                                el.hide();
                            }
                        });

                    });

                    scope.$watch(function () {
                        return (controller.$submitted || controller.$dirty) && !controller.$valid;
                    }, function () {
                        subForm.toggleClass("has-error", (controller.$submitted || controller.$dirty) && !controller.$valid);
                    });

                    scope.$on("pg-submitted", function () {
                        controller.$submitted = true;
                    });
                }
            }
        }])
        .directive('pgPasswordConfirm', [function () {
            return {
                require: "ngModel",
                link: function ($scope, element, attrs, ctrl) {
                    $scope.$watch('password + ' + attrs.ngModel,
                        function (value) {
                            var password = $scope.$eval(attrs.pgPasswordConfirm);
                            var confirmPassword = $scope.$eval(attrs.ngModel);
                            var valid = password == confirmPassword;
                            ctrl.$setValidity("passwordConfirm", valid);
                        });
                }
            };
        }])
        .directive('pgPricecell', [function () {
            return {
                scope: {
                    ngModel: "="
                },
                require: "ngModel",
                templateUrl: "resources/template/priceCellTemplate.html",
                link: function ($scope, element, attrs) {
                    $scope.field = attrs.pgPricecell;
                    $scope.currency = $scope.$parent.$eval("currency");
                }
            }
        }])
        .directive('pgDeliveryPlace', [function () {
            return {
                scope: {
                    ngModel: "=",
                    defaultInput: "@",
                    freeInput: "@",
                    onChangeAction: "&"
                },
                require: "?ngModel",
                templateUrl: "resources/template/deliveryPlace.html",
                link: function ($scope, element, attrs, ngModel) {
                    $scope.places = $scope.$eval(attrs.pgDeliveryPlace);
                    $scope.defaultInput = attrs.defaultInput;
                    $scope.freeInput = attrs.freeInput;

                    $scope.deliveryPlaceOrder = function (deliveryPlace) {
                        return  deliveryPlace.id;
                    }

                    $scope.setSelectedPlace = function (place) {
                        $scope.selectedPlace = place;
                        if (place != $scope.freeInput) {
                            $scope.updateNgModel(place);
                        } else {
                            $scope.updateNgModel($scope.freePlace);
                        }
                    }

                    $scope.updateNgModel = function (place) {
                        ngModel.$setViewValue(place);
                        if (!($scope.onChangeAction == null || $scope.onChangeAction == undefined)) {
                            $scope.onChangeAction(place);
                        }
                    }

                    var initialize = function (places, defaultInput) {
                        if (defaultInput == null || defaultInput == undefined || defaultInput == "") {
                            return;
                        }
                        _.map(places, function (place) {
                            if (place.name == defaultInput && place.name != $scope.freeInput) {
                                $scope.selectedPlace = place.name;
                                return;
                            }
                        });

                        if ($scope.selectedPlace != null) {
                            return;
                        }

                        $scope.selectedPlace = $scope.freeInput;
                        $scope.freePlace = defaultInput;
                    }

                    initialize($scope.places, $scope.defaultInput);

                    attrs.$observe('defaultInput', function (value) {
                        initialize($scope.places, value);
                    });

                }
            }
        }])
        .directive('pgTimePeriod', [function () {
            return {
                scope: {
                    fromTime: "=",
                    toTime: "=",
                    fromTimeLimit: "@",
                    toTimeLimit: "@"
                },
                restrict: 'E',
                templateUrl: "resources/template/timePeriod.html",
                link: function ($scope, element, attrs) {

                    $scope.limitFromTime = function () {
                        var timeLimit = attrs.fromTimeLimit;
                        if ($scope.fromTime.valueOf() < timeLimit) {
                            var timeLimitDate = new Date(parseInt(timeLimit));
                            $scope.fromTime = timeLimitDate;
                            attrs.fromTime = timeLimitDate;
                        }

                        if ($scope.toTime.valueOf() < $scope.fromTime.valueOf()) {
                            $scope.fromTime = $scope.toTime;
                            attrs.fromTime = $scope.toTime;
                        }
                    }

                    $scope.limitToTime = function () {
                        var timeLimit = attrs.toTimeLimit;

                        if (timeLimit < $scope.toTime.valueOf()) {
                            var timeLimitDate = new Date(parseInt(timeLimit));
                            $scope.toTime = timeLimitDate;
                            attrs.toTime = timeLimitDate;
                        }

                        if ($scope.toTime.valueOf() < $scope.fromTime.valueOf()) {
                            $scope.toTime = $scope.fromTime;
                            attrs.toTime = $scope.fromTime;
                        }
                    }

                    $scope.$watch("fromTimeLimit", function () {
                        $scope.limitFromTime()
                    });

                    $scope.$watch("toTimeLimit", function () {
                        $scope.limitToTime()
                    });

                }
            }
        }])
        .directive('pgPricelist', [function () {
            return {
                scope: {
                    pricelist: "=",
                    order: "=",
                    onSelectAction: "&"
                },
                restrict: 'E',
                templateUrl: "resources/template/priceList.html",
                link: function ($scope, element, attrs) {
                    $scope.priceList = $scope.pricelist;

                    function isSelectedPriceList(order) {
                        return order.priceListPosition == $scope.priceList.position && order.seller.id == $scope.priceList.user.id;
                    }

                    function updateView() {
                        var order = $scope.order;
                        if (order != null && order != undefined && isSelectedPriceList(order)) {
                        $scope.priceList = $scope.pricelist;
                            _.each(order.orderPositions, function (orderPosition) {
                                markSelectedPrice(orderPosition);
                            });
                        }
                    };

                    $scope.$watch("order", function () {
                        updateView();
                    }, true);

                    $scope.clickAction = function (priceListPosition, price) {
                        var product = priceListPosition.product;
                        $scope.product = product;
                        var currency = $scope.priceList.currency;
                        $scope.onSelectAction(
                            {data: {
                                priceList: $scope.priceList,
                                priceListPosition: priceListPosition,
                                price: price,
                                product: product,
                                currency: currency}
                            }
                        );
                    }

                    function markSelectedPrice(orderPosition) {
                        var priceListPosition = findPriceListPosition(orderPosition);
                        if (priceListPosition == undefined || priceListPosition == null) {
                            return orderPosition.price;
                        }
                        priceListPosition.amount = orderPosition.amount;
                        var prices = priceListPosition.prices;
                        var price = findPrice(prices, orderPosition.amount);
                        updatePricesSelected(prices, price, priceListPosition.amount);
                    }

                    function findPriceListPosition(orderPosition) {
                        return _.find($scope.priceList.positions, function (priceListPosition) {
                            return orderPosition.priceListPosition == priceListPosition.id;
                        });
                    }

                    function findPrice(prices, amount) {
                        var sortedPrices = _.sortBy(prices, function (price) {
                            return -1 * price.minOrderQuantity;
                        });

                        var price = _.find(sortedPrices, function (price) {
                            return price.minOrderQuantity <= amount;
                        });
                        return price;
                    }

                    function updatePricesSelected(prices, price, amount) {
                        _.map(prices, function (price) {
                            price.selectedStyle = "";
                            price.amount = "";
                        });
                        price.selectedStyle = "success";
                        price.amount = amount;
                    }
                }
            }
        }])
        .directive('pgOrder', [function () {
            return {
                scope: {
                    order: "=",
                    readonly: "@"
                },
                restrict: 'E',
                templateUrl: "resources/template/order.html",
                link: function ($scope, element, attrs) {
                    $scope.readonly = $scope.$eval(attrs.readonly);

                    $scope.$watch("readonly", function () {
                        $scope.readonly = $scope.$eval(attrs.readonly);
                    });
                }
            }
        }])
        .directive('pgShopPrices', [function () {
            return {
                scope: {
                    prices: "=",
                    currency: "=",
                    product: "=",
                    onDateChange: "&"
                },
                restrict: 'E',
                replace: true,
                templateUrl: "resources/template/shopPrices.html",
                compile: function compile(templateElement, templateAttrs) {
                    return {
                        pre: function ($scope, element, attrs) {
                            $scope.dateMax = new Date();
                            $scope.shopDate = new Date();

                            $scope.$watch("shopDate", function (newValue, oldValue) {
                                var matches = /^(?:(0[1-9]|1[012])[\-](0[1-9]|[12][0-9]|3[01])[\-](19|20)[0-9]{2})$/.exec(newValue);
                                if (newValue != oldValue && matches != null) {
                                    $scope.updateShopDate(newValue);
                                }
                            });

                            $scope.updateShopDate = function (shopDate) {
                                $scope.onDateChange({data: {shopDate: shopDate}});
                            };

                            //shop prices grid options
                            $scope.gridShopOptions = {
                                data: 'prices',
                                enableSorting: false,
                                plugins: [new ngGridFlexibleHeightPlugin({minHeight: 200})],
                                columnDefs: [
                                    {field: 'shop', displayName: 'Магазин', cellTemplate: 'resources/template/shopNameCellTemplate.html'},
                                    {field: 'price', displayName: 'Цена', cellTemplate: 'resources/template/yandexPriceCellTemplate.html'}
                                ]
                            };

                        }
                    }
                },
                link: function ($scope, element, attrs) {
                }
            }
        }])
        .directive('pgPriceDeltaChart', [function () {
            return {
                scope: {
                    monthNames: "=",
                    chart: "=",
                    currency: "="
                },
                restrict: 'E',
                templateUrl: "resources/template/priceDeltaChart.html",
                compile: function compile(templateElement, templateAttrs) {
                    return {
                        pre: function ($scope, element, attrs) {

                            $scope.fillChart = function () {
                                $scope.chartDatas = $scope.chart.data;
                                $scope.chartDetails = {
                                    grid: {
                                        hoverable: true,
                                        clickable: true
                                    },
                                    legend: {
                                        show: true,
                                        position: "nw"
                                    },
                                    xaxis: {
                                        show: true,
                                        mode: "time",
                                        min: $scope.chart.from,
                                        max: $scope.chart.to,
                                        monthNames: $scope.monthNames
                                    },
                                    yaxis: {
                                        tickFormatter: function formatter(val) {
                                            return val + "&nbsp;" + $scope.currency.symbol;
                                        }
                                    }
                                };

                            };
                            $scope.$watch("chart", function (newValue, oldValue) {
                                if (newValue != oldValue) {
                                    $scope.fillChart();
                                }
                            });
                        }
                    }
                },
                link: function ($scope, element, attrs, IndexChartResource, $cookieStore) {

                }
            }
        }])
        .directive('pgMarketUrl', [function () {
            return {
                scope: {
                    product: "="
                },
                restrict: 'E',
                replace: true,
                templateUrl: "resources/template/marketUrl.html",
                link: function ($scope, element, attrs) {

                    $scope.marketUrl = function () {
                        if ($scope.product == null || $scope.product == undefined || $scope.product == "") {
                            return "";
                        }

                        var exclude = $scope.product.excludeQuery ? "~~(" + $scope.product.excludeQuery.replace(/,/g, "|") + ")" : "";

                        var query = "(" + $scope.product.searchQuery.replace(/,/g, "|") + ")(" + $scope.product.colorQuery.replace(/,/g, "|") + ")" + exclude;

                        return "http://market.yandex.ru/search.xml?hid=" + $scope.product.type.yandexId + "&text=" + encodeURIComponent(query) + "&nopreciser=1&how=aprice&np=1&onstock=1";
                    };
                }
            }
        }])

    ;

})();