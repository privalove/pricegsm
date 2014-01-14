function AppCtrl() {

}

function IndexCtrl($scope, $timeout, $cookies, $filter, indexResource, IndexResource, IndexChartResource, IndexPriceResource, IndexShopResource) {

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

            $scope.getIndexShopResource({shopDate: $filter("date")(shopDate, 'dd.MM.yyyy') || $cookies.shopDate, product: product || $cookies.product}).then(function (indexShopResource) {
                if (indexShopResource.ok) {
                    angular.extend($scope, indexShopResource.payload);
                    $scope.fillCookies(indexShopResource.payload);

                    $scope.fillShopPrices();
                }
            });
        }, 300);
    };

    $scope.updateIndexPage = function(currency) {
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
            $cookies.shopDate = $filter("date")(new Date(payload.shopDate), 'dd.MM.yyyy');
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
        if ($scope.shopDate) {
            $scope.shopDate = new Date($scope.shopDate);
        }
        $scope.dateMax = new Date();

        var shopColumnDefs = [
            {field: 'position', displayName: "#", width:"5%"},
            {field: 'shop', displayName: 'Магазин', cellTemplate: 'resources/template/shopNameCellTemplate.html', width: '25%'}
        ];
        angular.forEach($scope.colors, function (value) {
            shopColumnDefs.push({field: 'id' + value.id, displayName: value.name});
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

IndexCtrl.resolve = {
    indexResource: function (IndexResource, $cookies) {
        //reset shop date to today on enter index page
        return getIndexResource(IndexResource, $cookies, {shopDate: null});
    }
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

MarketplaceCtrl.resolve = {

};

function OrderCtrl($scope) {

}

OrderCtrl.resolve = {

};

function SalesCtrl($scope) {

}

SalesCtrl.resolve = {

};

function PriceListCtrl($scope, priceListResource) {
    if (priceListResource.ok) {
        angular.extend($scope, priceListResource.payload);


    }
}

PriceListCtrl.resolve = {
    priceListResource: function(PriceListResource) {
        return PriceListResource.get().$promise;
    }
};

function PartnerCtrl($scope) {

}

PartnerCtrl.resolve = {

};

function ProfileCtrl($scope) {

}

ProfileCtrl.resolve = {

};
