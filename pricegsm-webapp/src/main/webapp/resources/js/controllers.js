AppCtrl.$inject = ["$scope"];
function AppCtrl($scope) {

}


IndexCtrl.$inject = ["$scope", "$cookieStore", "$filter", "$locale", "indexResource", "IndexResource", "IndexChartResource", "IndexShopResource"];
function IndexCtrl($scope, $cookieStore, $filter, $locale, indexResource, IndexResource, IndexChartResource, IndexShopResource) {

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

    $scope.getIndexShopResource = function (data) {

        $cookieStore.put("shopDate", data.shopDate || $cookieStore.get("shopDate"));
        $cookieStore.put("product", data.product || $cookieStore.get("product"));

        return IndexShopResource.get({
            product: $cookieStore.get("product"),
            shopDate: $cookieStore.get("shopDate"),
            currency: $cookieStore.get("currency")
        }).$promise;
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

function isEmpty(data) {
    return data == undefined || data == null || data == "";

}

MarketplaceCtrl.$inject = ["$scope", "$filter", "pricelists", "orders", "Order", "IndexShopResource"];
function MarketplaceCtrl($scope, $filter, pricelists, orders, Order, IndexShopResource) {

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

    var shopPricesData = {
        product: undefined,
        shopDate: new Date(),
        currency: undefined
    };

    $scope.getIndexShopResource = function () {

        return IndexShopResource.get({
            product: shopPricesData.product.id,
            shopDate: $filter("date")(shopPricesData.shopDate, 'yyyy-MM-dd'),
            currency: shopPricesData.currency.id
        }).$promise;
    };

    function isEmptyShopPricesData() {
        if (isEmpty(shopPricesData.product) || isEmpty(shopPricesData.currency) || isEmpty(shopPricesData.shopDate)) {
            return true;
        }
    }

    function updateShopPrices() {
        if (isEmptyShopPricesData()) {
            return;
        }

        $scope.getIndexShopResource()
            .then(function (indexShopResource) {
                if (indexShopResource.ok) {
                    $scope.safeApply(function () {
                        angular.extend($scope, indexShopResource.payload);
                    });
                }
            });
    };

    $scope.updateShopPrices = function (data) {
        angular.extend(shopPricesData, data);
        $scope.currency = shopPricesData.currency;
        updateShopPrices();
    }

    if (pricelists.ok) {
        $scope.pricelists = pricelists.payload.pricelists;
        $scope.selectedProduct = $scope.pricelists[0].product;

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
        var PriceList = $resource("order/:id/:position/pricelist.json", {id: '@id', position: '@position'});
        PriceList.get({id: currentOrder.seller.id, position: currentOrder.priceListPosition}, function (data) {
            if (currentOrder.status == "PREPARE") {
                orderModal = $modal.open({
                    templateUrl: "resources/template/orderPositionPrepare.html",
                    controller: OrderPositionCtrl,
                    size: "lg",
                    resolve: {
                        currentOrder: function () {
                            return currentOrder;
                        },
                        currentPriceList: function () {
                            return data.payload.priceList;
                        },
                        deliveryPlaces: function () {
                            return data.payload.deliveryPlaces;
                        }
                    }
                });
                orderModal.result.then(function (savedOrder) {
                    if (savedOrder.orderPositions.length != 0) {
                        saveOrder(savedOrder);
                    } else {
                        deleteOrder(savedOrder, getOrderIndex(savedOrder))
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
                        },
                        currentPriceList: function () {
                            return data.payload.priceList;
                        },
                        deliveryPlaces: function () {
                            return data.payload.deliveryPlaces;
                        }
                    }
                });
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

OrderPositionCtrl.$inject = ["$scope", "$modal", "$modalInstance", "$resource", "$filter", "currentOrder", "currentPriceList", "deliveryPlaces"];
function OrderPositionCtrl($scope, $modal, $modalInstance, $resource, $filter, currentOrder, currentPriceList, deliveryPlaces) {
    $scope.order = angular.copy(currentOrder);

    $scope.priceList = currentPriceList;

    $scope.deliveryPlaces = deliveryPlaces;

    $scope.deliveryDateFormat = R.get('order.format.deliveryDate');

    $scope.showSaveError = false;

    $scope.showActuallityError = false;

    $scope.isRefreshed = false;

    $scope.isShowRefreshedError = false;

    $scope.updateName = function () {
        if ($scope.order.contactName == null || $scope.order.contactName == undefined || $scope.order.contactName == "") {
            $scope.order.contactName = $scope.order.buyer.name;
        }
    }

    $scope.updatePhone = function () {
        if ($scope.order.phone == null || $scope.order.phone == undefined || $scope.order.phone == "") {
            $scope.order.phone = $scope.order.buyer.phone;
        }
    }

    $scope.updatePhone();

    if ($scope.order.seller.managerPhone == null || $scope.order.seller.managerPhone == undefined || $scope.order.seller.managerPhone == "") {
        $scope.order.seller.managerPhone = $scope.order.seller.phone;
    }
    //todo try to refactor DeliveryPlace start
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
            $scope.deliveryPlace == "";
        } else {
            $scope.deliveryPlace = $scope.order.place;
        }
    })();

    $scope.changeDeliveryPlace = function (place) {
        $scope.deliveryPlace = place;
    }
    //todo try to refactor DeliveryPlace end

    //todo create date list directive start
    var baseDateFormat = "yyyy-MM-dd";

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

    $scope.showPastDate = compareDates(new Date(), new Date($scope.order.deliveryDate)) > 0;

    function getDateList(startDate, numberOfDays) {
        var format = "yyyy-MM-dd";
        var dates = [];
        for (var i = 0; i < numberOfDays; i++) {
            dates.push($filter('date')(new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i), format));
        }
        return dates;
    }

    function getPossibleDeliveryDates() {
        if ($scope.priceList == null || $scope.priceList == undefined) {
            return;
        }
        var today = new Date();
        var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
        if ($scope.priceList.position == 0) {
            if (today.getHours() < $scope.order.seller.deadLine) {
                return getDateList(today, 3);
            } else {
                return getDateList(tomorrow, 2);
            }
        } else {
            var fromDate = new Date($scope.priceList.sellFromDate);
            var toDate = new Date($scope.priceList.sellToDate);
            if (compareDates(today, toDate) > 0) {
                return [];
            }
            if (compareDates(today, toDate) == 0) {
                if (today.getHours() < $scope.order.seller.deadLine) {
                    fromDate = today;
                } else {
                    return [];
                }
            }

            if (compareDates(today, fromDate) >= 0) {
                if (today.getHours() < $scope.order.seller.deadLine) {
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

    $scope.possibleDeliveryDates = getPossibleDeliveryDates();
    //todo create date list directive end

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

    $scope.limitFromTime = $scope.getLimitFromTime($scope.order.delivery);
    $scope.limitToTime = $scope.getLimitToTime($scope.order.delivery);

    $scope.resetOtherDelivery = function (selectedDeliveryType) {
        if (selectedDeliveryType != "delivery") {
            $scope.order.delivery = false;
        }
        if (selectedDeliveryType != "pickup") {
            $scope.order.pickup = false;
        }
        $scope.order.deliveryFree = false;

        $scope.limitFromTime = $scope.getLimitFromTime($scope.order.delivery);
        $scope.limitToTime = $scope.getLimitToTime($scope.order.delivery);
    }

    //todo PriceList and Order start
    $scope.findPriceListPosition = function (orderPosition) {
        return _.find($scope.priceList.positions, function (priceListPosition) {
            return orderPosition.priceListPosition == priceListPosition.id;
        });
    }

    if ($scope.priceList != null && $scope.priceList != undefined) {
        _.each($scope.order.orderPositions, function (orderPosition) {
            markSelectedPrice(orderPosition);
        });
    }

    $scope.updatePrices = function (orderPosition) {
        $scope.updatePriceListAmount(orderPosition);

        $scope.order.totalPrice = $scope.calcTotalPrice($scope.order);
        $scope.order.deliveryCost = $scope.calcDeliveryPrice($scope.order);

    }

    $scope.validateRefreshedState = function () {
        if (!$scope.isRefreshed) {
            $scope.isShowRefreshedError = true;
        }
        return $scope.isRefreshed;
    }

    $scope.changePrices = function (orderPosition) {
        if (!$scope.validateRefreshedState()) {
            return;
        }
        $scope.updatePrices(orderPosition);
    }

    $scope.refreshOrderPositions = function (order) {
        _.map(order.orderPositions, function (orderPosition) {
            var priceListPosition = $scope.findPriceListPosition(orderPosition);
            if (priceListPosition == undefined || priceListPosition == null) {
                orderPosition.selectedStyle = "danger";
                orderPosition.deleted = true;
                return;
            }
            var prices = priceListPosition.prices;
            var price = findPrice(prices, orderPosition.amount);
            $scope.updatePrices(orderPosition);
            orderPosition.specification = priceListPosition.specification;
            orderPosition.description = priceListPosition.description;

            if (priceListPosition.amount < orderPosition.amount) {
                orderPosition.amount = priceListPosition.amount;
            }

            if (priceListPosition.price != price.price) {
                orderPosition.price = price.price;
            }

            $scope.updatePriceListAmount(orderPosition);

        });
        if (order.orderPositions.length != 0) {
            $scope.order.totalPrice = $scope.calcTotalPrice($scope.order);
            $scope.order.deliveryCost = $scope.calcDeliveryPrice($scope.order);
        } else {
            $scope.order.totalPrice = 0;
            $scope.order.deliveryCost = 0;
        }
    };

    $scope.loadPriceList = function (sellerId, position, callback) {
        var PriceList = $resource("order/:id/:position/pricelist.json", {id: '@id', position: '@position'});
        PriceList.get({id: sellerId, position: position}, function (data) {

            $scope.isRefreshed = true;
            $scope.isShowRefreshedError = false;

            $scope.priceList = data.payload.priceList;
            $scope.orderPositionTemplate = data.payload.orderPositionTemplate;

            $scope.order.currency = $scope.priceList.currency;

            $scope.refreshOrderPositions($scope.order);

            if (callback != null && callback != undefined) {
                callback();
            }

        });
    };

    $scope.refresh = function (callback) {
        var today = new Date();

        if (compareDates(today, new Date($scope.order.deliveryDate)) > 0) {
            $scope.order.deliveryDate = $filter('date')(today, baseDateFormat);
            $scope.showPastDate = false;
            $scope.showSaveError = false;
        }

        var sellToDate = new Date($scope.priceList.sellToDate);
        var sellFromDate = new Date($scope.priceList.sellFromDate);
        var afterDeadline = today.getHours() >= new Date($scope.order.seller.deadLine).getHours();
        var comparedDatesFrom = compareDates(today, sellFromDate);
        var comparedDatesTo = compareDates(today, sellToDate);
        if ($scope.priceList.position > 0 && comparedDatesTo == 1
            || $scope.priceList.position > 0 && comparedDatesTo == 0 && afterDeadline
            || $scope.priceList.position == 0 && comparedDatesFrom > 0
            || $scope.priceList.position == 0 && comparedDatesFrom == 0 && afterDeadline) {

            $scope.showActuallityError = true;
            return;
        }
        $scope.showActuallityError = false;

        $scope.loadPriceList($scope.order.seller.id, $scope.order.priceListPosition, callback);
    }

    $scope.reduceAmount = function (orderPosition) {
        if (!$scope.validateRefreshedState()) {
            return;
        }

        if (orderPosition.amount > $scope.getMinimumAmount(orderPosition)) {
            orderPosition.amount--;
            $scope.updatePrices(orderPosition);
        }
    }

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

        $scope.order.orderPositions.push(position);
        $scope.updatePrices(position);
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

    var calculatePrice = function (prices, amount) {
        var price = findPrice(prices, amount);
        return  price.price;
    };

    function updatePricesSelected(prices, price, amount) {
        _.map(prices, function (price) {
            price.selectedStyle = "";
            price.amount = "";
        });
        price.selectedStyle = "success";
        price.amount = amount;
    }

    function markSelectedPrice(orderPosition) {
        var priceListPosition = $scope.findPriceListPosition(orderPosition);
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

    $scope.updatePriceListAmount = function (orderPosition) {
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

    $scope.getMinimumAmount = function (orderPosition) {
        var priceListPosition = $scope.findPriceListPosition(orderPosition);
        if (priceListPosition == undefined || priceListPosition == null) {
            return orderPosition.amount;
        }
        var price = _.min(priceListPosition.prices, function (price) {
            return price.minOrderQuantity;
        });
        return  price.minOrderQuantity;
    }

    $scope.formEnable = function (orderForm) {
        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
            return orderPosition.amount == 0;
        });
        return exitingOrderPosition == undefined
            && orderForm.$valid
            && ($scope.order.delivery || $scope.order.pickup)
            && !($scope.order.delivery && $scope.deliveryPlaceAvailability
            && ($scope.deliveryPlace == null || $scope.deliveryPlace == undefined || $scope.deliveryPlace == ""))
            && !$scope.showActuallityError;
    }

    $scope.getPositionTotalPrice = function (orderPosition) {
        var amount = orderPosition.amount;
        if (amount == undefined || orderPosition.deleted) {
            return 0;
        }
        return  orderPosition.price * amount;
    }

    $scope.calcDeliveryPrice = function (order) {
        if (!$scope.order.seller.sellerDeliveryPaid && !$scope.order.seller.sellerDeliveryFree) {
            return 0;
        }

        if ($scope.order.seller.sellerDeliveryPaid && !$scope.order.seller.sellerDeliveryFree) {
            return $scope.order.seller.sellerDeliveryCost;
        }

        if (!$scope.order.seller.sellerDeliveryPaid && $scope.order.seller.sellerDeliveryFree) {
            return 0;
        }
        if ($scope.order.seller.sellerDeliveryPaid && $scope.order.seller.sellerDeliveryFree) {
            if ($scope.calcTotalAmount(order) >= $scope.order.seller.sellerDeliveryMin) {
                return 0;
            } else {
                return  $scope.order.seller.sellerDeliveryCost;
            }
        }
    }

    function getPositionTotalPrice(order) {
        var positionTotalPrice = _.reduce(
            _.map(order.orderPositions, function (position) {
                return $scope.getPositionTotalPrice(position)
            }),
            function (memo, num) {
                return memo + num
            }, 0);
        return positionTotalPrice;
    }

    $scope.calcTotalPrice = function (order) {
        return  getPositionTotalPrice(order) + $scope.calcDeliveryPrice(order);
    }

    $scope.deleteOrderPosition = function ($event, index) {
        $event.stopPropagation();
        $scope.order.orderPositions.splice(index, 1);
        if ($scope.priceList != null) {
            $scope.refresh();
        }
    }

    $scope.save = function (form, status) {
        if (compareDates(new Date(), new Date($scope.order.deliveryDate)) > 0) {
            $scope.showSaveError = true;
            return;
        }
        if (form.$valid) {
            $scope.refresh(function () {
                $scope.order.totalAmount = $scope.calcTotalAmount($scope.order);
                $scope.order.status = status;
                $scope.order.totalPrice = $scope.calcTotalPrice($scope.order);
                $scope.order.deliveryCost = $scope.calcDeliveryPrice($scope.order);
                $scope.updatePhone();
                $scope.updateName()
                if ($scope.order.pickup) {
                    $scope.order.place = $scope.order.seller.sellerPickupPlace;
                } else if ($scope.deliveryPlaceAvailability) {
                    $scope.order.place = $scope.deliveryPlace;
                } else {
                    $scope.order.place = $scope.order.seller.sellerDeliveryPlace;
                }
                $scope.ok($scope.order);
            });
        }
    };

    $scope.calcTotalAmount = function (order) {

        return _.reduce(
            _.map(order.orderPositions, function (position) {
                if (position.deleted) {
                    return 0;
                }
                return position.amount;
            }),
            function (memo, num) {
                return memo + num;
            }, 0)
    }

    $scope.review = function (order) {
        $modal.open({
            templateUrl: "resources/template/orderPosition.html",
            controller: OrderPositionCtrl,
            size: "lg",
            resolve: {
                currentOrder: function () {
                    return order;
                },
                currentPriceList: function () {
                    return null;
                },
                deliveryPlaces: function () {
                    return null;
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

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.open = function ($event) {
        $event.preventDefault();

        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.ok = function (resultOrder) {
        $modalInstance.close(resultOrder);
    }
}

angular.module('orderFilters', [])
    .filter('sellerDeliveryDate', function () {
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
    .filter('deliveryFilter',function () {
        return function (order) {
            if (order.delivery) {
                return R.get('order.delivery.delivery');
            }
            if (order.pickup) {
                return R.get('order.delivery.pickup');
            }
        }
    }).filter('priceFilter', function () {
        return function (price, currencySymbol) {
            return "" + currencySymbol + price;
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

    //todo ask
    var baseDateFormat = "yyyy-MM-dd";

    $scope.extend = function (form, index) {
        var priceList = $scope.priceLists[index];

//        priceList.active = true;

//        var date = new Date();
        //todo deadline logic
//        priceList.sellFromDate = $filter('date')(new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1), baseDateFormat);
//        priceList.sellToDate = $filter('date')(new Date(date.getFullYear(), date.getMonth(), date.getDate() + 2), baseDateFormat);

        $scope.save(form, index);
    }

    $scope.extendAvailability = function (form, index) {
        var priceList = $scope.priceLists[index];
        var date1 = new Date();
        var date2 = new Date(priceList.sellFromDate);
        date2.setHours(17);
        date2.setMinutes(0);
        return date1 < date2 || !form.$valid;
    }

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

        if ($scope.user.managerPhone == null || $scope.user.managerPhone == undefined || $scope.user.managerPhone == "") {
            $scope.user.managerPhone = $scope.user.phone;
        }

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
            $scope.addPrice(position);

            priceList.positions.push(position);
        };

        $scope.addPrice = function (position) {
            position.prices.push(angular.copy($scope.priceTemplate));
        }

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
        angular.extend($scope, context.payload);

        $scope.profileForm = profileForm;
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
