AppCtrl.$inject = ["$scope"];
function AppCtrl($scope) {

}


IndexCtrl.$inject = ["$scope", "$cookieStore", "$filter", "$locale", "indexResource", "IndexResource", "IndexChartResource", "IndexShopResource"];
function IndexCtrl($scope, $cookieStore, $filter, $locale, indexResource, IndexResource, IndexChartResource, IndexShopResource) {

    $scope.marketUrl = function () {

        var exclude = $scope.product.excludeQuery ? "~(" + $scope.product.excludeQuery.replace(/,/g, "|") + ")" : "";

        var query = "(" + $scope.product.searchQuery + ")(" + $scope.product.colorQuery.replace(/,/g, "|") + ")" + exclude;

        return "http://market.yandex.ru/search.xml?hid=" + $scope.product.type.yandexId + "&text=" + encodeURIComponent(query) + "&nopreciser=1&how=aprice&np=1&onstock=1";
    };

    $scope.getIndexChartResource = function (data) {

        $cookieStore.put("chartData", data.chartData || $cookieStore.get("chartData"));
        $cookieStore.put("product", data.product || $cookieStore.get("product"));

        return IndexChartResource.get({
            product: $cookieStore.get("product"),
            chartData: $cookieStore.get("chartData"),
            dynRange: $cookieStore.get("dynRange"),
            currency: $cookieStore.get("currency")
        }).$promise;
    };

    $scope.getIndexShopResource = function (data) {

        $cookieStore.put("shopDate", data.shopDate || $cookieStore.get("shopDate"));
        $cookieStore.put("product", data.product || $cookieStore.get("product"));

        return IndexShopResource.get({
            product: $cookieStore.get("product"),
            shopDate: $cookieStore.get("shopDate"),
            currency: $cookieStore.get("currency")
        }).$promise;
    };

    $scope.updateChart = function () {
        $scope.getIndexChartResource({
            dynRange: $scope.dynRange,
            product: $scope.product.id})
            .then(function (indexChartResource) {
                if (indexChartResource.ok) {
                    $scope.safeApply(function () {
                        angular.extend($scope, indexChartResource.payload);
                        $scope.fillChart();
                    });
                }
            });
    };

    $scope.updateShopPrices = function () {
        $scope.getIndexShopResource({
            shopDate: $filter("date")($scope.shopDate, 'yyyy-MM-dd'),
            product: $scope.product.id})
            .then(function (indexShopResource) {
                if (indexShopResource.ok) {
                    $scope.safeApply(function () {
                        angular.extend($scope, indexShopResource.payload);
                        $scope.fillShopPrices();
                    });
                }
            });
    };

    $scope.updateIndexPage = function (currency, vendor) {
        $scope.currencyDisabled = true;

        getIndexResource(IndexResource, $cookieStore, {
            currency: currency,
            dynRange: $scope.dynRange,
            vendor: vendor})
            .then(function (indexResource) {
                if (indexResource.ok) {
                    $scope.safeApply(function () {
                        angular.extend($scope, indexResource.payload);
                        $scope.fillCookies(indexResource.payload);
                        $scope.fillChart();
                    });
                }

                $scope.currencyDisabled = false;
            });
    };


    $scope.fillCookies = function (payload) {
        if (payload.product) {
            $cookieStore.put("product", payload.product.id);
        }
        if (payload.product) {
            $cookieStore.put("vendor", payload.product.vendor.id);
        }
    };

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
                monthNames: $locale.DATETIME_FORMATS.SHORTMONTH
            },
            yaxis: {
                tickFormatter: function formatter(val) {
                    return val + "&nbsp;" + $scope.currency.symbol;
                }
            }
        };
    };


    $scope.fillPrices = function () {
    };

    $scope.fillShopPrices = function () {
        $scope.dateMax = new Date();
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
        enableSorting: false,
        plugins: [new ngGridFlexibleHeightPlugin({minHeight: 200})],
        columnDefs: [
            {field: 'shop', displayName: 'Магазин', cellTemplate: 'resources/template/shopNameCellTemplate.html'},
            {field: 'price', displayName: 'Цена', cellTemplate: 'resources/template/yandexPriceCellTemplate.html'}
        ]
    };

    //on index resource load
    if (indexResource.ok) {
        angular.extend($scope, indexResource.payload);

        $scope.fillCookies(indexResource.payload);
        $scope.fillChart();
        $scope.fillPrices();
        $scope.fillShopPrices();

        $scope.$watch("currency.id", function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.updateIndexPage(newValue);
            }
        });

        $scope.updateProduct = function (product) {
            $scope.product = product;
            $scope.updateShopPrices();
            $scope.updateChart();
        };

    }
}

IndexCtrl.resolve = {
    indexResource: ["IndexResource", "$cookieStore", function (IndexResource, $cookieStore) {
        //reset shop date to today on enter index page
        return getIndexResource(IndexResource, $cookieStore, {shopDate: null});
    }]
};

function getIndexResource(IndexResource, $cookieStore, data) {

    $cookieStore.put("product", data.product || $cookieStore.get("product") || 1);
    $cookieStore.put("chartData", data.chartData || $cookieStore.get("chartData") || "retail");
    $cookieStore.put("currency", data.currency || $cookieStore.get("currency") || 1);
    $cookieStore.put("dynRange", data.dynRange || $cookieStore.get("dynRange") || 7);
    $cookieStore.put("vendor", data.vendor || $cookieStore.get("vendor") || 1);
    $cookieStore.put("shopDate", data.shopDate || $cookieStore.get("shopDate"));

    return IndexResource.get({
        product: $cookieStore.get("product"),
        chartData: $cookieStore.get("chartData"),
        currency: $cookieStore.get("currency"),
        dynRange: $cookieStore.get("dynRange"),
        shopDate: $cookieStore.get("shopDate"),
        vendor: $cookieStore.get("vendor")
    }).$promise;
}

MarketplaceCtrl.$inject = ["$scope"];
function MarketplaceCtrl($scope) {

}

MarketplaceCtrl.resolve = {

};

OrderCtrl.$inject = ["$scope"];
function OrderCtrl($scope) {

}

OrderCtrl.resolve = {

};

SalesCtrl.$inject = ["$scope"];
function SalesCtrl($scope) {

}

SalesCtrl.resolve = {

};

PriceListCtrl.$inject = ["$scope", "$filter", "notifyManager", "priceListResource", "PriceList"];
function PriceListCtrl($scope, $filter, notifyManager, priceListResource, PriceList) {

    $scope.fetchVendors = function (positions) {
        return  $filter("unique")(_.map(positions, function (position) {
            return position.product.vendor
        }), "id");
    };

    $scope.save = function (form, index) {
        var priceList = $scope.priceLists[index];
        priceList.position = index;

        if (form.$valid) {
            priceList.$save({priceListId: index}, function (data) {
                if (data.ok) {
                    notifyManager.success("Прайс лист успешно сохранен");
                }
                $scope.priceLists[index] = new PriceList(data.payload.priceList);

            })
        }
    };

    if (priceListResource.ok) {
        angular.extend($scope, priceListResource.payload);

        var priceLists = [];

        angular.forEach($scope.priceLists, function (priceList) {
            priceLists.push(new PriceList(priceList));
        });

        $scope.priceLists = priceLists;

        $scope.fakePositions = _.object(_.map($scope.products, function (product) {
            var fakePosition = {};

            fakePosition[product.id] = {fake: true, product: product, productId: product.id};
            return [product.id, {fake: true, product: product}];
        }));

        $scope.selectedVendors = _.map($scope.priceLists, function(priceList){
           return $scope.fetchVendors(priceList.positions);
        });

        $scope.showAll = {};

        $scope.nextCurrency = _.map($scope.priceLists, function (priceList) {
            return priceList.currency
        });
        $scope.exchangeRate = [];

        $scope.preOrderPriceLists = function () {
            return $scope.priceLists.slice(1);
        };

        $scope.freeVendors = function (index) {
            var ids = _.map($scope.selectedVendors[index], function (vendor) {
                return vendor.id;
            });

            return _.filter($scope.vendors, function (vendor) {
                return !_.contains(ids, vendor.id)
            });
        };

        $scope.productsByVendor = function (vendor) {
            return  $filter("unique")(_.filter($scope.products, function (product) {
                return product.vendor.id == vendor
            }), "name");
        };

        /**
         * @param positions Price List positions.
         * @param vendor Filtered vendor.
         * @param showFake If true show missed products as fake positions.
         * @returns {*}
         */
        $scope.filterByVendor = function (positions, vendor, showFake) {

            var result = _.filter(positions, function (position) {
                return position.product.vendor.id == vendor
            });

            //add missed products as fake positions
            if (showFake) {

                var ids = _.map(result, function (position) {
                    return position.product.id;
                });

                var products = _.filter($scope.products, function (product) {
                    return product.vendor.id == vendor
                });

                _.each(products, function (product) {
                    if (!_.contains(ids, product.id)) {
                        result.push($scope.fakePositions[product.id]);
                    }
                });
            }

            return _.sortBy(result, function (position) {
                return position.product.name + position.product.color.name
            });
        };


        $scope.addPreOrder = function () {
            $scope.priceLists.push(new PriceList($scope.template));
        };

        $scope.addPosition = function (priceList, product) {

            var position = angular.copy($scope.positionTemplate);
            position.product = product;

            priceList.positions.push(position);
        };

        $scope.addVendor = function (vendor) {
            $scope.selectedVendors.push(vendor);
        };

        $scope.setNextCurrency = function (currency, index) {
            if ($scope.priceLists[index].currency.id != currency.id) {
                var exchangeRate = _.find($scope.exchanges, function (exchange) {
                    return exchange.to.id == currency.id && exchange.from.id == $scope.priceLists[index].currency.id;
                });

                if (!exchangeRate) {
                    exchangeRate = _.find($scope.exchanges, function (exchange) {
                        return exchange.from.id == currency.id && exchange.to.id == $scope.priceLists[index].currency.id;
                    });
                    $scope.exchangeRate[index] = (1 / exchangeRate.value).toFixed(3);
                } else {
                    $scope.exchangeRate[index] = exchangeRate.value;
                }

            }

            $scope.nextCurrency[index] = currency;
        };

        $scope.changeCurrency = function(index) {
            _.each($scope.priceLists[index].positions, function(position) {
                position.price = Math.round(position.price * $scope.exchangeRate[index]);
            });

            $scope.priceLists[index].currency = $scope.nextCurrency[index];
        }
    }
}

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
