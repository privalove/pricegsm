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
                    deliveryPlaces: "=",
                    defaultInput: "@",
                    freeInput: "@",
                    onChangeAction: "&"
                },
                restrict: 'E',
                require: "?ngModel",
                templateUrl: "resources/template/deliveryPlace.html",
                link: function ($scope, element, attrs, ngModel) {
                    $scope.places = $scope.deliveryPlaces;
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
        .directive('pgDeliverySelection', [function () {
            return {
                scope: {
                    order: "=",
                    deliveryPlaces: "=",
                    updateTimeLimits: "&"
                },
                replace: true,
                restrict: 'E',
                templateUrl: "resources/template/deliverySelection.html",
                link: function ($scope, element, attrs, ngModel) {

                    var calculateDeliveryPlaceAvailability = function () {
                        var currentDeliveryPlace = null;
                        _.map($scope.deliveryPlaces, function (deliveryPlace) {
                            if (deliveryPlace.name == $scope.order.seller.sellerDeliveryPlace) {
                                currentDeliveryPlace = deliveryPlace;
                                return;
                            }
                        });

                        return currentDeliveryPlace != null && currentDeliveryPlace.name == $scope.order.seller.region.name;
                    }

                    $scope.deliveryPlaceAvailability = calculateDeliveryPlaceAvailability();
                    $scope.deliveryPlace = null;

                    (function () {
                        if ($scope.order.pickup == true) {
                            $scope.deliveryPlace = "";
                        } else {
                            $scope.deliveryPlace = $scope.order.place;
                        }
                    })();

                    $scope.getLimitFromTime = function (isDelivery) {
                        if (isDelivery) {
                            return $scope.order.seller.sellerDeliveryFrom;
                        } else {
                            return $scope.order.seller.sellerPickupFrom;
                        }
                    }

                    $scope.getLimitToTime = function (isDelivery) {
                        if (isDelivery) {
                            return $scope.order.seller.sellerDeliveryTo;
                        } else {
                            return $scope.order.seller.sellerPickupTo;
                        }
                    }

                    function updateTimeLimits() {
                        var limitFromTime = $scope.getLimitFromTime($scope.order.delivery);
                        var limitToTime = $scope.getLimitToTime($scope.order.delivery);
                        $scope.updateTimeLimits({limits: {from: limitFromTime, to: limitToTime}});
                    };
                    updateTimeLimits();

                    $scope.resetOtherDelivery = function (selectedDeliveryType) {
                        if (selectedDeliveryType != "delivery") {
                            $scope.order.delivery = false;
                        }
                        if (selectedDeliveryType != "pickup") {
                            $scope.order.pickup = false;
                        }
                        $scope.order.deliveryFree = false;

                        updateTimeLimits();
                    }

                    $scope.changeDeliveryPlace = function (place) {
                        $scope.deliveryPlace = place;
                        if ($scope.order.pickup) {
                            $scope.order.place = $scope.order.seller.sellerPickupPlace;
                        } else if ($scope.deliveryPlaceAvailability) {
                            $scope.order.place = $scope.deliveryPlace;
                        } else {
                            $scope.order.place = $scope.order.seller.sellerDeliveryPlace;
                        }
                    }
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
                        if (isEmpty(timeLimit)) {
                            return;
                        }
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
                        if (isEmpty(timeLimit)) {
                            return;
                        }
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
        .directive('pgDateListSelector', [function () {
            return {
                scope: {
                    priceList: "=",
                    order: "="
                },
                restrict: 'E',
                templateUrl: "resources/template/dateList.html",
                link: function ($scope, element, attrs) {
                    var priceList = $scope.priceList;
                    var order = $scope.order;

//                    $scope.deliveryDateFormat = R.get('order.format.deliveryDate');
                    $scope.deliveryDateFormat = "dd MMM, EEE";

                    function compareDates(date1, date2) {
                        date1 = new Date(date1.getFullYear(), date1.getMonth(), date1.getDate());
                        date2 = new Date(date2.getFullYear(), date2.getMonth(), date2.getDate());
                        if (date1.valueOf() > date2.valueOf()) {
                            return 1;
                        }
                        if (date1.valueOf() == date2.valueOf()) {
                            return 0;
                        }
                        if (date1.valueOf() < date2.valueOf()) {
                            return -1;
                        }
                    }

                    $scope.showPastDate = compareDates(new Date(), new Date(order.deliveryDate)) > 0;

                    function getDateList(startDate, numberOfDays) {
                        var format = "yyyy-MM-dd";
                        var dates = [];
                        for (var i = 0; i < numberOfDays; i++) {
                            dates.push(new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i));
                        }
                        return dates;
                    }

                    function getPossibleDeliveryDates(priceList2) {
                        priceList2 = priceList2 || $scope.priceList;
                        if (priceList2 == null || priceList2 == undefined) {
                            return;
                        }
                        var today = new Date();
                        var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
                        if (priceList2.position == 0) {
                            if (today.getHours() < priceList2.user.deadLine) {
                                return getDateList(today, 3);
                            } else {
                                return getDateList(tomorrow, 2);
                            }
                        } else {
                            var fromDate = new Date(priceList2.sellFromDate);
                            var toDate = new Date(priceList2.sellToDate);
                            if (compareDates(today, toDate) > 0) {
                                return [];
                            }
                            if (compareDates(today, toDate) == 0) {
                                if (today.getHours() < priceList2.user.deadLine) {
                                    fromDate = today;
                                } else {
                                    return [];
                                }
                            }

                            if (compareDates(today, fromDate) >= 0) {
                                if (today.getHours() < priceList2.user.deadLine) {
                                    fromDate = today;
                                } else {
                                    fromDate = tomorrow;
                                }
                            }

                            var dayNumber = toDate.getDate();
                            if (toDate.getMonth() != fromDate.getMonth()) {
                                toDate.setDate(-1)
                                dayNumber += toDate.getDate() + 1;
                            }
                            var numberOfDays = dayNumber - fromDate.getDate() + 1;
                            return getDateList(fromDate, numberOfDays);
                        }
                    }

                    $scope.possibleDeliveryDates = getPossibleDeliveryDates(priceList);

                    $scope.$watch("order", function () {
                        priceList = $scope.priceList;
                        order = $scope.order;
                        $scope.possibleDeliveryDates = getPossibleDeliveryDates(priceList);
                        $scope.showPastDate = compareDates(new Date(), new Date(order.deliveryDate)) > 0;
                    }, true);
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
                }
            }
        }])
        .directive('pgOrder', [function () {
            return {
                scope: {
                    order: "=",
                    readonly: "@",
                    updateOrder: "&"
                },
                restrict: 'E',
                templateUrl: "resources/template/orderTable.html",
                link: function ($scope, element, attrs) {
                    $scope.readonly = $scope.$eval(attrs.readonly);

                    $scope.$watch("readonly", function () {
                        $scope.readonly = $scope.$eval(attrs.readonly);
                    });

                    $scope.deleteOrderPosition = function ($event, index) {
                        $scope.order.orderPositions.splice(index, 1);
                        $scope.updateOrder($scope.order);
                    }

                    $scope.reduceAmount = function (orderPosition) {
                        if (orderPosition.minOrderQuantity < orderPosition.amount) {
                            orderPosition.amount--;
                            $scope.updateOrder($scope.order);
                        }
                    }

                    $scope.updateAmount = function () {
                        $scope.updateOrder($scope.order);
                    }
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
        .directive('pgSaveOrderButton', ["$resource", "notifyManager", function ($resource, notifyManager) {
            return {
                scope: {
                    order: "=",
                    callback: "&"
                },
                restrict: 'A',
                replace: true,
                link: function ($scope, element, attrs) {

                    element.click(function () {
                        saveOrder();
                    });

                    function saveOrder() {
                        var Order = $resource("order/:order/order.json");

                        var order = $scope.order;
                        order.sendDate = new Date();
                        order.status = attrs.pgSaveOrderButton;
                        new Order(order).$save(function (data) {
                            if (data.ok) {
                                notifyManager.success("Заказ успешно отправлен");
                                $scope.callback();
                            }
                        });
                    }
                }
            }
        }])
        .directive('pgLoadPriceList', ["$resource", function ($resource) {
            return {
                scope: {
                    userId: "@",
                    position: "@",
                    callback: "&"
                },
                restrict: 'A',

                link: function ($scope, element, attrs) {

                    element.click(function () {
                        refresh();
                    });

                    function refresh() {
                        loadPriceList(attrs.userId, attrs.position);
                    }

                    function loadPriceList(sellerId, position) {
                        var PriceList = $resource("order/:id/:position/pricelist.json", {id: '@id', position: '@position'});
                        PriceList.get({id: sellerId, position: position}, function (data) {

                            if (data.ok) {
                                $scope.callback({data: {priceList: data.payload.priceList}});
                            }
                        });
                    };
                }
            }
        }])

    ;

})();