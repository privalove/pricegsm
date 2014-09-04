AppCtrl.$inject = ["$scope"];
function AppCtrl($scope) {

}


IndexCtrl.$inject = ["$scope", "$cookieStore", "$filter", "$locale", "indexResource", "IndexResource", "IndexChartResource", "IndexShopResource"];
function IndexCtrl($scope, $cookieStore, $filter, $locale, indexResource, IndexResource, IndexChartResource, IndexShopResource) {

    $scope.marketUrl = function () {

        var exclude = $scope.product.excludeQuery ? "~~(" + $scope.product.excludeQuery.replace(/,/g, "|") + ")" : "";

        var query = "(" + $scope.product.searchQuery.replace(/,/g, "|") + ")(" + $scope.product.colorQuery.replace(/,/g, "|") + ")" + exclude;

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

MarketplaceCtrl.$inject = ["$scope", "pricelists", "orders", "Order"];
function MarketplaceCtrl($scope, pricelists, orders, Order) {

    $scope.findOrDefaultOrderPosition = function (seller, pricelistPosition) {
        var result;

        var order = _.find($scope.orders, function (order) {
            return order.seller.id == seller.id;
        });

        if (order != null) {
            result = _.find(order.orderPositions, function (position) {
                return position.priceListPosition == pricelistPosition.id;
            });
        }

        if (result == null) {
            result = {
                amount: 0
            }
        }

        return result;
    };


    $scope.findOrCreateOrderPosition = function (seller, pricelistPosition) {
        var result;

        var order = _.find($scope.orders, function (order) {
            return order.seller.id == seller.id;
        });

        if (order != null) {
            result = _.find(order.orderPositions, function (position) {
                return position.pricelistPosition == pricelistPosition.id;
            });
        }


        if (order == null) {
            order = {
                seller: seller,
                orderPositions: []
            };

            $scope.orders.push(order);
        }

        if (result == null) {
            result = {
                order: order,
                pricelistPosition: pricelistPosition.id,
                product: pricelistPosition.product,
                price: pricelistPosition.price,
                specification: pricelistPosition.specification,
                amount: 0
            };
            order.orderPositions.push(result);
        }

        return result;

    };

    if (pricelists.ok) {
        $scope.pricelists = pricelists.payload.pricelists;

        $scope.updateAmount = function (seller, position, amount) {
            $scope.findOrCreateOrderPosition(seller, position).amount = amount;
        };

        $scope.calcTotal = function (order) {

            return _.reduce(
                _.map(order.orderPositions, function (position) {
                    return position.amount * position.price
                }),
                function (memo, num) {
                    return memo + num
                }, 0)
        }
    }

    if (orders.ok) {
        $scope.orders = orders.payload.orders;
    }
}

MarketplaceCtrl.resolve = {
    "pricelists": ["PriceLists", function (PriceLists) {
        return PriceLists.get().$promise;
    }],
    "orders": ["Orders", function (Orders) {
        return Orders.get().$promise;
    }]
};

OrderCtrl.$inject = ["$scope", "$filter", "$modal", "$resource", "orders", "notifyManager"];
function OrderCtrl($scope, $filter, $modal, $resource, orders, notifyManager) {
    if (orders.ok) {
        $scope.orders = orders.payload.orders;
    }

    function getOrderIndex(order) {
        var actualIndex = null;
        _.map($scope.orders, function (currentOrder, index) {
            if (currentOrder.id == order.id) {
                actualIndex = index;
            }
            return currentOrder;
        });
        return actualIndex;
    }

    $scope.orderDetails = function (currentOrder) {
        var orderModal;
        if (currentOrder.status == "PREPARE") {
            orderModal = $modal.open({
                templateUrl: "resources/template/orderPositionPrepare.html",
                controller: OrderPositionCtrl,
                size: "lg",
                resolve: {
                    currentOrder: function () {
                        return currentOrder;
                    }
                }
            });
        } else {
            orderModal = $modal.open({
                templateUrl: "resources/template/orderPosition.html",
                controller: OrderPositionCtrl,
                size: "lg",
                resolve: {
                    currentOrder: function () {
                        return currentOrder;
                    }
                }
            });
        }
        orderModal.result.then(function (savedOrder) {
            if (savedOrder.orderPositions.length != 0) {
                saveOrder(savedOrder);
            } else {
                deleteOrder(savedOrder, getOrderIndex(savedOrder))
            }
        });
    }

    function saveOrder(savedOrder) {
        var Order = $resource("order/:order/:orderId/order.json", {orderId: '@id'});
        new Order(savedOrder).$save({orderId: savedOrder.id}, function (data) {
            if (data.ok) {
                updateOrderList(data.payload.order);
                notifyManager.success("Заказ успешно отправлен");
            }
        });
    }

    var updateOrderList = function (savedOrder) {
        var actualIndex = getOrderIndex(savedOrder);
        if (actualIndex != null) {
            $scope.orders.splice(actualIndex, 1);
            $scope.orders.push(savedOrder);
        }
    }

    function deleteOrder(order, index) {
        var DeleteOrder = $resource('order/:orderId/delete', {orderId: '@id'});
        DeleteOrder.delete({orderId: order.id}, function () {
            $scope.orders.splice(index, 1)
            notifyManager.success("Заказ успешно удалён");
        });
    }

    $scope.deleteOrder = function ($event, index, order) {
        $event.stopPropagation();
        deleteOrder(order, index);
    }

    $scope.deliveryDates = $filter("unique")(_.map($scope.orders, function (order) {
        return order.deliveryDate;
    }));

    $scope.sellers = $filter("unique")(_.map($scope.orders, function (order) {
        return order.seller;
    }), "id");
    $scope.sendDateFormat = R.get('order.format.sendDate');

    $scope.deliveryDateFormat = R.get('order.format.deliveryDate');

    $scope.orderStatus = function (order) {
        if (order.status == "PREPARE") {
            return 1;
        }

        if (order.status == "SENT") {
            return 2;
        }

        if (order.status == "CANCELED") {
            return 3;
        }

        if (order.status == "CONFIRMED") {
            return 4;
        }

        if (order.status == "DECLINED") {
            return 5;
        }
    };

    $scope.deliveryDateOrder = function (order) {
        return new Date(order.deliveryDate);
    };
}

OrderCtrl.resolve = {
    "orders": ["OrdersOrderPage", function (Orders) {
        return Orders.get().$promise;
    }]
};

OrderPositionCtrl.$inject = ["$scope", "$modal", "$modalInstance", "$resource", "$filter", "currentOrder"];
function OrderPositionCtrl($scope, $modal, $modalInstance, $resource, $filter, currentOrder) {
    $scope.order = angular.copy(currentOrder);

    $scope.priceList = null;

    $scope.deliveryDateFormat = R.get('order.format.deliveryDate');

    var baseDateFormat = "yyyy-MM-dd";
    var today = new Date();
    var todayString = $filter('date')(today, baseDateFormat);
    var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
    var tomorrowString = $filter('date')(tomorrow, baseDateFormat);
    var deliveryDate = new Date($scope.order.deliveryDate);
    deliveryDate = new Date(deliveryDate.getFullYear(), deliveryDate.getMonth(), deliveryDate.getDate(), today.getHours(), today.getMinutes(), today.getSeconds(), today.getMilliseconds());
    if (today >= deliveryDate) {
        $scope.order.deliveryDate = todayString;
    }
    if (today.getHours() < 21) {
        $scope.possibleDeliveryDates = [todayString, tomorrowString];
    } else {
        var afterTomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 2);
        var afterTomorrowString = $filter('date')(afterTomorrow, baseDateFormat);
        $scope.possibleDeliveryDates = [todayString, tomorrowString, afterTomorrowString];
    }

    $scope.findPriceListPosition = function (orderPosition) {
        return _.find($scope.priceList.positions, function (priceListPosition) {
            return orderPosition.priceListPosition == priceListPosition.id;
        });
    }

    $scope.refreshOrderPositions = function (order) {

        $scope.order.orderPositions = _.reject(order.orderPositions, function (orderPosition) {
            var priceListPosition = $scope.findPriceListPosition(orderPosition);
            return priceListPosition == undefined;
        });

        _.map(order.orderPositions, function (orderPosition) {
            var priceListPosition = $scope.findPriceListPosition(orderPosition);
            priceListPosition.selectedStyle = "success";

            if (priceListPosition.amount < orderPosition.amount) {
                orderPosition.amount = priceListPosition.amount;
            }

            if (priceListPosition.price != orderPosition.price) {
                orderPosition.price = priceListPosition.price;
            }

            $scope.calcTotalPrice($scope.order);

            $scope.updatePriceListAmount(orderPosition);
        });
    };

    $scope.loadPriceList = function (sellerId, position, callback) {
        var PriceList = $resource("order/:id/:position/pricelist.json", {id: '@id', position: '@position'});
        PriceList.get({id: sellerId, position: position}, function (data) {
            $scope.priceList = data.payload.priceList;
            $scope.orderPositionTemplate = data.payload.orderPositionTemplate;
            $scope.refreshOrderPositions($scope.order);
            _.map($scope.priceList.positions, function (priceListPosition) {
                priceListPosition.amount = 0;
            });
            _.map($scope.order.orderPositions, function (orderPosition) {
                $scope.updatePriceListAmount(orderPosition);
            });
            if (callback != null && callback != undefined) {
                callback();
            }
        });
    };

    $scope.refresh = function (callback) {
        $scope.loadPriceList($scope.order.seller.id, $scope.order.priceListPosition, callback);
    }

    $scope.showPriceList = function () {
        $scope.showHidePriceList = !$scope.showHidePriceList;
        if ($scope.showHidePriceList) {
            $scope.refresh();
        }
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };


    $scope.open = function ($event) {
        $event.preventDefault();

        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.addOrderPosition = function (priceListPosition) {

        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
            return orderPosition.priceListPosition == priceListPosition.id;
        });

        if (exitingOrderPosition != undefined) {
            return;
        }

        var position = angular.copy($scope.orderPositionTemplate);
        position.order = {id: currentOrder.id, name: ""};
        position.totalPrice = 0;
        position.product = priceListPosition.product;
        position.priceListPosition = priceListPosition.id;

        $scope.order.orderPositions.push(position);
        $scope.refreshOrderPositions($scope.order);
    }

    $scope.updatePriceListAmount = function (orderPosition) {
        if ($scope.priceList != null) {
            var position = $scope.findPriceListPosition(orderPosition);
            position.amount = orderPosition.amount;
        }
        if (orderPosition.amount == 0) {
            orderPosition.selectedStyle = "danger";

        } else {
            orderPosition.selectedStyle = "";
        }

        orderPosition.totalPrice = $scope.getPositionTotalPrice(orderPosition);
    }

    $scope.formEnable = function (orderForm) {
        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
            return orderPosition.amount == 0;
        });
        return exitingOrderPosition == undefined
            && orderForm.$valid
            && ($scope.order.delivery || $scope.order.deliveryFree || $scope.order.pickup);
    }

    $scope.resetOtherDelivery = function (selectedDeliveryType) {
        if (selectedDeliveryType != "delivery") {
            $scope.order.delivery = false;
        }
        if (selectedDeliveryType != "pickup") {
            $scope.order.pickup = false;
        }
        $scope.order.deliveryFree = false;
    }

    $scope.getPositionTotalPrice = function (orderPosition) {
        var amount = orderPosition.amount;
        if (amount == undefined) {
            return 0;
        }
        return  orderPosition.price * amount;
    }

    $scope.calcTotalPrice = function (order) {

        return _.reduce(
            _.map(order.orderPositions, function (position) {
                return $scope.getPositionTotalPrice(position)
            }),
            function (memo, num) {
                return memo + num
            }, 0)
    }

    $scope.deleteOrderPosition = function (index) {
        $scope.order.orderPositions.splice(index, 1);
        if ($scope.priceList != null) {
            $scope.refresh()
        }
    }

    $scope.save = function (form, status) {

        if (form.$valid) {
            $scope.refresh(function () {
                $scope.order.totalAmount = $scope.calcTotalAmount($scope.order);
                $scope.order.status = status;
                $scope.order.totalPrice = $scope.calcTotalPrice($scope.order);
                $scope.ok($scope.order);
            });
        }
    };

    $scope.review = function (order) {
        $modal.open({
            templateUrl: "resources/template/orderPosition.html",
            controller: OrderPositionCtrl,
            size: "lg",
            resolve: {
                currentOrder: function () {
                    return order;
                }
            }
        });
    }

    $scope.print = function () {
        $scope.review($scope.order);
        var printContents = document.getElementById("orderView").innerHTML;
        var popupWin = window.open('', '_blank');
        popupWin.document.open()
        popupWin.document.write('<html><head><link href="/pricegsm/resources/components/bootstrap/dist/css/bootstrap.min.css" type="text/css" rel="stylesheet"></head><body onload="window.print()">' + printContents + '</html>');
        popupWin.document.close();
        $scope.cancel();
    }

    $scope.ok = function (resultOrder) {
        $modalInstance.close(resultOrder);
    }

    $scope.calcTotalAmount = function (order) {

        return _.reduce(
            _.map(order.orderPositions, function (position) {
                return position.amount;
            }),
            function (memo, num) {
                return memo + num;
            }, 0)
    }

}

angular.module('orderFilters', []).filter('sellerDeliveryDate', function () {
    return function (orders, sellerId, deliveryDate) {

        var resultOrders = [];

        orders.forEach(function (order) {
            if ((sellerId === undefined || sellerId == "" || order.seller.id == sellerId)
                && (deliveryDate === undefined || deliveryDate == "" || order.deliveryDate == deliveryDate )) {
                resultOrders.push(order);
            }
        });

        return resultOrders;
    };
})
    .filter('statusFilter', function () {
        return function (status) {
            if (status == "PREPARE") {
                return R.get('order.status.prepare');
            }

            if (status == "SENT") {
                return R.get('order.status.sent');
            }

            if (status == "CANCELED") {
                return R.get('order.status.canceled');
            }

            if (status == "CONFIRMED") {
                return R.get('order.status.confirmed');
            }

            if (status == "DECLINED") {
                return R.get('order.status.declined');
            }
        };
    })
    .filter('deliveryFilter', function () {
        return function (order) {
            if (order.delivery) {
                return R.get('order.delivery.delivery');
            }

            if (order.deliveryFree) {
                return R.get('order.delivery.deliveryFree');
            }

            if (order.pickup) {
                return R.get('order.delivery.pickup');
            }
        }
    });

SalesCtrl.$inject = ["$scope"];
function SalesCtrl($scope) {

}

SalesCtrl.resolve = {

};

PriceListCtrl.$inject = ["$scope", "$filter", "notifyManager", "priceListResource", "PriceList", "WorkConditions"];
function PriceListCtrl($scope, $filter, notifyManager, priceListResource, PriceList, WorkConditions) {

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

        $scope.selectedVendors = _.map($scope.priceLists, function (priceList) {
            return $scope.fetchVendors(priceList.positions);
        });

        $scope.showAll = {};

        $scope.nextCurrency = _.map($scope.priceLists, function (priceList) {
            return priceList.currency
        });
        $scope.exchangeRate = [];

        $scope.user = new WorkConditions($scope.user);

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


        $scope.addPositionNextColor = function (priceList, currentProductName, currentProductColorId) {

            var productList = $filter("unique")(_.filter($scope.products, function (product) {
                return product.name == currentProductName;
            }), "color");

            var indexNewElement = 0;

            for (var i = 0; i < productList.length; i++) {
                if (currentProductColorId == productList[i].color.id) {
                    if (i == productList.length - 1) {
                        indexNewElement = 0;
                        break;
                    } else {
                        indexNewElement = i + 1;
                        break;
                    }
                }
            }
            var position = angular.copy($scope.positionTemplate);
            position.product = productList[indexNewElement];

            priceList.positions.push(position);
        };

        $scope.addVendor = function (vendor, index) {
            $scope.selectedVendors[index].push(vendor);
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

        $scope.changeCurrency = function (index) {
            _.each($scope.priceLists[index].positions, function (position) {
                position.price = Math.round(position.price * $scope.exchangeRate[index]);
            });

            $scope.priceLists[index].currency = $scope.nextCurrency[index];
        };

        $scope.updateWorkConditions = function (form) {
            if (form.$valid) {
                $scope.user.$save(function (data) {
                    if (data.ok) {
                        notifyManager.success("Условия работы успешно сохранены");
                    }
                    $scope.user = new WorkConditions(data.payload.user);
                });
            }
        };
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

UserChangePasswordCtrl.$inject = ["$scope", "$modalInstance", "notifyManager", "ChangePassword"];
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

ProfileCtrl.$inject = ["$scope", "$location", "$modal", "notifyManager", "profileForm", "context", "Profile"];
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

ProfileCtrl.resolve = {
    context: ["ProfileContext", function (ProfileContext) {
        return ProfileContext.get().$promise;
    }],
    profileForm: ["Profile", function (Profile) {
        return Profile.get().$promise;
    }]
};
