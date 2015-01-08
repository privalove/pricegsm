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
    }).filter('priceFilter',function () {
        return function (price, currencySymbol) {
            return "" + currencySymbol + price;
        }
    }).filter('productsByVendor',function () {
        return function (products, vendor) {
            function getUniqueProductByName(products) {
                var productGroupedByName = _.groupBy(products, function (product) {
                    return product.name;
                });
                return _.map(productGroupedByName, function (productsGroup) {
                    return productsGroup[0];
                });
            }

            if (!isEmpty(vendor)) {
                var productsByVendor = _.filter(products, function (product) {
                    return vendor.id == product.vendor.id
                });
                return getUniqueProductByName(productsByVendor);
            } else {
                return getUniqueProductByName(products);
            }
        }
    }).filter('pricelistBySeller',function () {
        return function (pricelists, seller) {
            if (!isEmpty(seller)) {
                return _.filter(pricelists, function (pricelist) {
                    return seller.id == pricelist.user.id
                });
            } else {
                return pricelists;
            }
        }
    }).filter('pricelistPositionsByVendorAndProduct', function () {
        return function (pricelistPositions, vendor, product) {
            if (!isEmpty(product)) {
                return _.filter(pricelistPositions, function (position) {
                    return product.name == position.product.name
                });

            } else if (!isEmpty(vendor)) {
                return _.filter(pricelistPositions, function (position) {
                    return vendor.id == position.product.vendor.id
                });
            } else {
                return pricelistPositions;
            }
        }
    });
