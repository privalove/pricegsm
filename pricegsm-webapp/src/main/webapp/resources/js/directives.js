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
                        $.plot(chartContainer, $scope.pgChart, $scope.chartDetails);
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
        }]).directive('pgTimePeriod', [function () {
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
        }]).directive('pgPriceList', [function () {
            return {
                scope: {
                    order: "@"
                },
                require: "?ngModel",
                templateUrl: "resources/template/priceList.html",
                link: function ($scope, element, attrs) {
                    $scope.priceList = attrs.pgPriceList;

                    if (attrs.order != null && attrs.order != undefined) {
                        _.each(attrs.order.orderPositions, function (orderPosition) {
                            markSelectedPrice(orderPosition);
                        });
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
                        //todo bad practice 2 responsib refsctor
                        return price;
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

                    function updatePriceListAmount(orderPosition) {
                        if ($scope.priceList != null) {
                            var price = markSelectedPrice(orderPosition);
                            orderPosition.price = price.price;
                        }
                        if (orderPosition.amount == 0) {
                            orderPosition.selectedStyle = "danger";

                        } else {
                            orderPosition.selectedStyle = "";
                        }

                        orderPosition.totalPrice = $scope.getPositionTotalPrice(orderPosition);
                    }

                    //todo add Order createon
                    $scope.addOrderPosition = function (priceListPosition, price) {
                        if (!$scope.validateRefreshedState()) {
                            return;
                        }
                        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
                            return orderPosition.priceListPosition == priceListPosition.id;
                        });

                        if (exitingOrderPosition != undefined) {
                            if (price.minOrderQuantity > exitingOrderPosition.amount) {
                                priceListPosition.amount = price.minOrderQuantity;
                                exitingOrderPosition.amount = price.minOrderQuantity;
                                exitingOrderPosition.price = price.price;
                            } else {
                                priceListPosition.amount++;
                                exitingOrderPosition.amount++;
                            }

                            $scope.refreshOrderPositions($scope.order);
                            exitingOrderPosition.totalPrice = $scope.getPositionTotalPrice(exitingOrderPosition);
                            return;
                        }

                        var position = angular.copy($scope.orderPositionTemplate);
                        position.order = {id: currentOrder.id, name: ""};
                        priceListPosition.amount = price.minOrderQuantity;
                        position.amount = price.minOrderQuantity;
                        position.price = price.price;
                        position.totalPrice = 0;
                        position.product = priceListPosition.product;
                        position.priceListPosition = priceListPosition.id;
                        position.price = calculatePrice(priceListPosition.prices, position.amount);
                        position.specification = priceListPosition.specification;
                        position.description = priceListPosition.description;

                        order.orderPositions.push(position);
                        updatePrices(position);
                    }

                     function updatePrices(orderPosition) {
                        $scope.updatePriceListAmount(orderPosition);

                        $scope.order.totalPrice = $scope.calcTotalPrice($scope.order);
                        $scope.order.deliveryCost = $scope.calcDeliveryPrice($scope.order);

                    }

                }
            }
        }]).directive('pgOrder', [function () {
            return {
                scope: {
                },
                require: "?ngModel",
                templateUrl: "resources/template/order.html",
                link: function ($scope, element, attrs) {

                }
            }
        }])

    ;

})();