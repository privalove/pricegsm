function AppCtrl() {

}

function IndexCtrl($scope, $cookies, indexResource, IndexResource, IndexChartResource) {
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

    $scope.getIndexChartResource = function (data) {
        return IndexChartResource.get(angular.extend({
            product: $cookies.product,
            chartData: $cookies.chartData,
            chartRange: $cookies.chartRange,
            currency: $cookies.currency
        }, data)).$promise;
    };

    if (indexResource.ok) {
        angular.extend($scope, indexResource.payload);

        $scope.fillCookies(indexResource.payload);
        $scope.fillChart();

        $scope.gridPriceOptions = {
            data: 'prices',
            groups: ['vendor'],
            multiSelect: false,
            plugins: [new ngGridFlexibleHeightPlugin()],
            columnDefs: [
                {field: 'vendor', displayName: '', width: 0},
                {field: 'product', displayName: 'Наименование'},
                {field: 'color', displayName: 'Цвет'},
                {field: 'retail', displayName: 'Розница'},
                {field: 'opt', displayName: 'Опт'},
                {field: 'world', displayName: 'Мир'}
            ]
        };

        var shopColumnDefs = [
            {field: 'shop', displayName: 'Магазин'}
        ];
        angular.forEach($scope.colors, function (value) {
            shopColumnDefs.push({field: value});
        });

        $scope.gridShopOptions = {
            data: 'yandexPrices',
            plugins: [new ngGridFlexibleHeightPlugin({minHeight: 200})],
            columnDefs: shopColumnDefs
        };

        $scope.$watch("chartRange", function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.getIndexChartResource({chartRange: newValue}).then(function (indexChartResource) {
                    if (indexChartResource.ok) {
                            angular.extend($scope, indexChartResource.payload);

                            $scope.fillCookies(indexChartResource.payload);
                            $scope.fillChart();
                    }
                });
            }

        });

        $scope.$watch("currency", function (newValue, oldValue) {
            if (newValue != oldValue) {
                getIndexResource(IndexResource, $cookies, {currency: newValue}).then(function (indexResource) {
                    if (indexResource.ok) {
                            angular.extend($scope, indexResource.payload);

                            $scope.fillCookies(indexResource.payload);
                            $scope.fillChart();
                    }
                });
            }
        });

    }
}

IndexCtrl.resolve = {
    indexResource: function (IndexResource, $cookies) {
        return getIndexResource(IndexResource, $cookies, {});
    }
};

function getIndexResource(IndexResource, $cookies, data) {
    return IndexResource.get(angular.extend({
        product: $cookies.product,
        chartData: $cookies.chartData,
        chartRange: $cookies.chartRange,
        currency: $cookies.currency,
        dynRange: $cookies.dynRange
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

function PriceListCtrl($scope) {

}

PriceListCtrl.resolve = {

};

function PartnerCtrl($scope) {

}

PartnerCtrl.resolve = {

};

function ProfileCtrl($scope) {

}

ProfileCtrl.resolve = {

};
