function AppCtrl() {

}

function IndexCtrl($scope, $timeout, $cookies, $filter, indexResource, IndexResource, IndexChartResource, IndexPriceResource, IndexShopResource) {

    $scope.marketUrl = function () {
        return "http://market.yandex.ru/offers.xml?modelid="
            + $scope.product.yandexId + "&hid=" + $scope.product.type.yandexId
            + "&grhow=shop&how=aprice&np=1&onstock=1"
    };

    $scope.getIndexChartResource = function (data) {
        return IndexChartResource.get(angular.extend({
            product: $cookies.product,
            chartData: $cookies.chartData,
            chartRange: $cookies.chartRange,
            currency: $cookies.currency
        }, data)).$promise;
    };

    $scope.getIndexPriceResource = function (data) {
        return IndexPriceResource.get(angular.extend({
            product: $cookies.product,
            dynRange: $cookies.dynRange,
            currency: $cookies.currency
        }, data)).$promise;
    };

    $scope.getIndexShopResource = function (data) {
        return IndexShopResource.get(angular.extend({
            product: $cookies.product,
            shopDate: $cookies.shopDate,
            currency: $cookies.currency
        }, data)).$promise;
    };


    $scope.updateChart = function (chartRange, product) {

        if ($scope.chartTimeout) {
            clearTimeout($scope.chartTimeout);
        }

        $scope.chartTimeout = setTimeout(function () {
            $scope.getIndexChartResource({chartRange: chartRange || $cookies.chartRange, product: product || $cookies.product}).then(function (indexChartResource) {
                if (indexChartResource.ok) {
                    angular.extend($scope, indexChartResource.payload);

                    $scope.fillCookies(indexChartResource.payload);
                    $scope.fillChart();
                }
            });

        }, 300);
    };

    $scope.updatePrices = function (dynRange) {

        if ($scope.priceTimeout) {
            clearTimeout($scope.priceTimeout);
        }
        $scope.priceTimeout = setTimeout(function () {
            $scope.getIndexPriceResource({dynRange: dynRange || $cookies.dynRange}).then(function (indexPriceResource) {
                if (indexPriceResource.ok) {
                    angular.extend($scope, indexPriceResource.payload);
                    $scope.fillCookies(indexPriceResource.payload);

                    $scope.fillPrices();
                }
            });
        }, 300);
    };

    $scope.updateShopPrices = function (shopDate, product) {
        if ($scope.shopTimeout) {
            clearTimeout($scope.shopTimeout);
        }
        $scope.shopTimeout = setTimeout(function () {

            $scope.getIndexShopResource({shopDate: $filter("date")(shopDate, 'yyyy-MM-dd') || $cookies.shopDate, product: product || $cookies.product}).then(function (indexShopResource) {
                if (indexShopResource.ok) {
                    angular.extend($scope, indexShopResource.payload);
                    $scope.fillCookies(indexShopResource.payload);

                    $scope.fillShopPrices();
                }
            });
        }, 300);
    };

    $scope.updateIndexPage = function (currency) {
        if ($scope.indexTimeout) {
            $timeout.cancel($scope.indexTimeout);
        }

        $scope.currencyDisabled = true;

        $scope.indexTimeout = $timeout(function () {

            getIndexResource(IndexResource, $cookies, {currency: currency}).then(function (indexResource) {
                if (indexResource.ok) {
                    angular.extend($scope, indexResource.payload);

                    $scope.fillCookies(indexResource.payload);
                    $scope.fillChart();
                }

                $scope.currencyDisabled = false;
            });

        });
    };


    $scope.fillCookies = function (payload) {
        if (payload.product) {
            $cookies.product = payload.product.id + "";
        }
        if (payload.chartData) {
            $cookies.chartData = payload.chartData;
        }
        if (payload.chartRange) {
            $cookies.chartRange = payload.chartRange + "";
        }
        if (payload.currency) {
            $cookies.currency = payload.currency + "";
        }
        if (payload.dynRange) {
            $cookies.dynRange = payload.dynRange + "";
        }
        if (payload.shopDate) {
            $cookies.shopDate = payload.shopDate;
        }
    };

    $scope.fillChart = function () {
        $scope.chartDatas = [];
        angular.forEach($scope.chart.data, function (data, color) {
            $scope.chartDatas.push({
                data: data,
                label: color
            })
        });

        $scope.chartDetails = {
            legend: {show: true},
            xaxis: {show: true, mode: "time", min: $scope.chart.from, max: $scope.chart.to}
        };
    };


    $scope.fillPrices = function () {
    };

    $scope.fillShopPrices = function () {
        $scope.dateMax = new Date();

        var shopColumnDefs = [
            {field: 'position', displayName: "#", width: "5%"},
            {field: 'shop', displayName: 'Магазин', width: '25%'}
        ];
        angular.forEach($scope.colors, function (value) {
            shopColumnDefs.push({field: 'id' + value.id, displayName: value.name, cellTemplate: 'resources/template/shopNameCellTemplate.html'});
        });

        $scope.safeApply(function () {
            $scope.shopPricesColumns = shopColumnDefs;
        });
    };

    //datepicker settings
    $scope.dateOptions = {
        'year-format': "'yy'",
        'starting-day': 1
    };


    $scope.selectedProduct = [];

    //prices grid options
    $scope.gridPriceOptions = {
        data: 'prices',
        groups: ['vendor'],
        multiSelect: false,
        plugins: [new ngGridFlexibleHeightPlugin()],
        selectedItems: $scope.selectedProduct,
        groupsCollapsedByDefault: false,
        enableSorting: false,
        columnDefs: [
            {field: 'vendor', displayName: '', width: 0},
            {field: 'product', displayName: 'Наименование', width: "25%"},
            {field: 'color', displayName: 'Цвет'},
            {field: 'retail', displayName: 'Розница', cellTemplate: "resources/template/priceCellTemplate.html"},
            {field: 'opt', displayName: 'Опт', cellTemplate: "resources/template/priceCellTemplate.html"},
            {field: 'world', displayName: 'Мир', cellTemplate: "resources/template/priceCellTemplate.html"}
        ]
    };

    //shop prices grid options
    $scope.gridShopOptions = {
        data: 'yandexPrices',
        plugins: [new ngGridFlexibleHeightPlugin({minHeight: 200})],
        sortInfo: {
            fields: ['position'],
            directions: ['asc']
        },
        columnDefs: 'shopPricesColumns'
    };

    //on index resource load
    if (indexResource.ok) {
        angular.extend($scope, indexResource.payload);

        $scope.fillCookies(indexResource.payload);
        $scope.fillChart();
        $scope.fillPrices();
        $scope.fillShopPrices();

        $scope.$watch("currency", function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.updateIndexPage(newValue);
            }
        });

        $scope.$watch(function () {
                return $scope.selectedProduct.length > 0 ? $scope.selectedProduct[0].id : 0;
            },
            function (newVlaue, oldValue) {
                if (newVlaue > 0 && newVlaue != oldValue) {
                    $scope.updateShopPrices(null, newVlaue);
                    $scope.updateChart(null, newVlaue);
                }
            });

        $scope.$on("ngGridEventData", function () {
            angular.forEach($scope.prices, function (price, index) {
                if (price.id == $scope.product.id) {
                    $scope.gridPriceOptions.selectItem(index, true);
                }
            });
        });

    }
}

IndexCtrl.$inject = ["$scope", "$timeout", "$cookies", "$filter", "indexResource", "IndexResource", "IndexChartResource", "IndexPriceResource", "IndexShopResource"];

IndexCtrl.resolve = {
    indexResource: ["IndexResource", "$cookies", function (IndexResource, $cookies) {
        //reset shop date to today on enter index page
        return getIndexResource(IndexResource, $cookies, {shopDate: null});
    }]
};

function getIndexResource(IndexResource, $cookies, data) {
    return IndexResource.get(angular.extend({
        product: $cookies.product,
        chartData: $cookies.chartData,
        chartRange: $cookies.chartRange,
        currency: $cookies.currency,
        dynRange: $cookies.dynRange,
        shopDate: $cookies.shopDate
    }, data)).$promise;
}

function MarketplaceCtrl($scope) {

}

MarketplaceCtrl.$inject = ["$scope"];

MarketplaceCtrl.resolve = {

};

function OrderCtrl($scope) {

}

OrderCtrl.$inject = ["$scope"];

OrderCtrl.resolve = {

};

function SalesCtrl($scope) {

}

SalesCtrl.$inject = ["$scope"];

SalesCtrl.resolve = {

};

function PriceListCtrl($scope, priceListResource) {
    if (priceListResource.ok) {
        angular.extend($scope, priceListResource.payload);


    }
}

PriceListCtrl.$inject = ["$scope", "priceListResource"];

PriceListCtrl.resolve = {
    priceListResource: ["PriceListResource", function (PriceListResource) {
        return PriceListResource.get().$promise;
    }]
};

function PartnerCtrl($scope) {

}

PartnerCtrl.$inject = ["$scope"];

PartnerCtrl.resolve = {

};

function UserChangePasswordCtrl($scope, $modalInstance, notifyManager, ChangePassword) {
    $scope.changePasswordForm = new ChangePassword({});

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.change = function (cpf) {
        if (cpf.$valid) {
            $scope.changePasswordForm.$save(
                function (data) {
                    if (data.ok) {
                        notifyManager.success(R.get(data.message));
                    } else {
                        notifyManager.error(R.get(data.message));
                    }
                    $modalInstance.close();
                });
        }
    };
}

UserChangePasswordCtrl.$inject = ["$scope", "$modalInstance", "notifyManager", "ChangePassword"];

function ProfileCtrl($scope, $location, $modal, notifyManager, profileForm, context, Profile) {

    $scope.changePassword = function () {
        $modal.open({
            templateUrl: "resources/template/changePasswordTemplate.html",
            controller: UserChangePasswordCtrl
        });
    };

    $scope.updateProfile = function () {
        if ($scope.profileFormForm.$valid) {
            profileForm.$save(function (data) {
                if (data.ok) {
                    notifyManager.success(R.get('alert.profileUpdated'));
                    $location.url("/");
                } else {
                    if (data.payload && data.payload.profile) {
                        $scope.profileForm = new Profile(data.payload.profile);
                    }
                }
            })

        } else {
            $scope.$broadcast("pg-submitted");
        }
    };

    if (context.ok) {
        $scope.profileForm = profileForm;

        angular.extend($scope, context.payload);
    }
}

ProfileCtrl.$inject = ["$scope", "$location", "$modal", "notifyManager", "profileForm", "context", "Profile"];

ProfileCtrl.resolve = {
    context: ["ProfileContext", function (ProfileContext) {
        return ProfileContext.get().$promise;
    }],
    profileForm: ["Profile", function (Profile) {
        return Profile.get().$promise;
    }]
};
