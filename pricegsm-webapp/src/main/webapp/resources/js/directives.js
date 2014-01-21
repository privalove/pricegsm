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
                link: function($scope, element, attrs) {
                    $scope.field = attrs.pgPricecell;
                }
            }
        }])

    ;

})();