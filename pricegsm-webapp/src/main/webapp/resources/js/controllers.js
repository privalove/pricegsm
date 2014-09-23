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

    $scope.deliveryPlaceOrder = function (deliveryPlace) {
        return  deliveryPlace.id;
    }

    $scope.selectedPlace = null;

    var savedOrderPlace = null;

    var calculateSelectedPlace = function () {
        $scope.selectedPlace = null;
        _.map($scope.deliveryPlaces, function (deliveryPlace) {
            if (deliveryPlace.name == savedOrderPlace && deliveryPlace.name != $scope.order.seller.region.name) {
                $scope.selectedPlace = deliveryPlace.name;
                $scope.order.place = savedOrderPlace;
                return;
            }
        });
        if ($scope.selectedPlace != null) {
            return;
        }

        $scope.order.place = savedOrderPlace;
        if (savedOrderPlace == null || savedOrderPlace == undefined || savedOrderPlace == "") {
            return;
        }

        $scope.selectedPlace = $scope.order.seller.region.name;
    }

    $scope.setPlace = function (place) {
        $scope.order.place = place;
    };

    (function () {
        if ($scope.order.delivery == true) {
            if (!calculateDeliveryPlaceAvailability()) {
                $scope.setPlace($scope.order.seller.sellerDeliveryPlace)
            } else {
                savedOrderPlace = $scope.order.place;
                calculateSelectedPlace();
            }
        } else if ($scope.order.pickup == true) {
            $scope.setPlace($scope.order.seller.sellerPickupPlace)
        }
    })();

    $scope.setSelectedPlace = function (place) {
        $scope.selectedPlace = place;

        if (place != $scope.order.seller.region.name) {
            $scope.order.place = place;
        } else {
            var currentSelectedPlace = null;
            _.map($scope.deliveryPlaces, function (deliveryPlace) {
                if (deliveryPlace.name == savedOrderPlace && deliveryPlace.name != $scope.order.seller.region.name) {
                    currentSelectedPlace = place;
                }
            });
            if (currentSelectedPlace != null) {
                $scope.order.place = "";
            } else {
                $scope.order.place = savedOrderPlace;
            }
        }
    }

    $scope.calculateDeliveryPlaceFromDelivery = function (place) {
        if (calculateDeliveryPlaceAvailability()) {
            calculateSelectedPlace();
        } else {
            $scope.setPlace(place);
        }
    }

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
            if (compareDates(today, toDate) >= 0) {
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

    $scope.limitFromTime = function (isDelivery) {
        var timeLimit;

        if (isDelivery) {
            timeLimit = $scope.order.seller.sellerDeliveryFrom;
        } else {
            timeLimit = $scope.order.seller.sellerPickupFrom;
        }
        if ($scope.order.fromTime < timeLimit) {
            $scope.order.fromTime = timeLimit;
        }

        if ($scope.order.toTime < $scope.order.fromTime) {
            $scope.order.fromTime = $scope.order.toTime;
        }
    }

    $scope.limitToTime = function (isDelivery) {
        var timeLimit;
        if (isDelivery) {
            timeLimit = $scope.order.seller.sellerDeliveryTo;
        } else {
            timeLimit = $scope.order.seller.sellerPickupTo;
        }

        if (timeLimit < $scope.order.toTime) {
            $scope.order.toTime = timeLimit;
        }

        if ($scope.order.toTime < $scope.order.fromTime) {
            $scope.order.toTime = $scope.order.fromTime;
        }
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

    $scope.showPriceList = function () {
        if (!$scope.showHidePriceList) {
            $scope.refresh();
        }
        $scope.showHidePriceList = !$scope.showHidePriceList && !$scope.showActuallityError;
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.open = function ($event) {
        $event.preventDefault();

        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.reduceAmount = function (orderPosition) {
        var priceListPosition = $scope.findPriceListPosition(orderPosition);
        if (orderPosition.amount > priceListPosition.moq) {
            orderPosition.amount--;
            $scope.updatePriceListAmount(orderPosition);
        }
    }

    $scope.addOrderPosition = function (priceListPosition) {

        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
            return orderPosition.priceListPosition == priceListPosition.id;
        });

        if (exitingOrderPosition != undefined) {
            priceListPosition.amount++;
            exitingOrderPosition.amount++;
            $scope.refreshOrderPositions($scope.order);
            exitingOrderPosition.totalPrice = $scope.getPositionTotalPrice(exitingOrderPosition);
            return;
        }

        var position = angular.copy($scope.orderPositionTemplate);
        position.order = {id: currentOrder.id, name: ""};
        position.amount = priceListPosition.moq;
        priceListPosition.amount = priceListPosition.moq;
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

    $scope.getMinimumAmount = function (orderPosition) {
        var priceListPosition = $scope.findPriceListPosition(orderPosition);
        return priceListPosition.moq;
    }

    $scope.formEnable = function (orderForm) {
        var exitingOrderPosition = _.find($scope.order.orderPositions, function (orderPosition) {
            return orderPosition.amount == 0;
        });
        return exitingOrderPosition == undefined
            && orderForm.$valid
            && ($scope.order.delivery || $scope.order.deliveryFree || $scope.order.pickup)
            && !($scope.order.place == null || $scope.order.place == undefined || $scope.order.place == "")
            && !$scope.showActuallityError;
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

    $scope.deleteOrderPosition = function ($event, index) {
        $event.stopPropagation();
        $scope.order.orderPositions.splice(index, 1);
        if ($scope.priceList != null) {
            $scope.refresh()
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
                $scope.updatePhone();
                $scope.updateName()
                if ($scope.order.place == null || $scope.order.place == undefined) {
                    $scope.order.place = $scope.order.seller.sellerPickupPlace;
                }
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

            var phone = priceList.phone;
            if (phone == null || phone == undefined || phone == "") {
                priceList.phone = priceList.user.phone;
            }

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

        _.each($scope.priceLists, function (priceList) {
            var phone = priceList.phone;
            if (phone == null || phone == undefined || "") {
                priceList.phone = priceList.user.phone;
            }
        });

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
