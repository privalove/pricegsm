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
    }).filter('productsByVendor', function () {
        return function (vendor, products) {
            return _.find(products, function(product){
                return vendor.id == product.vendor.id });
        }
    }).filter('pricelistBySeller', function () {
        return function (seller, pricelists) {
            return _.find(pricelists, function(pricelist){
                return seller.id == pricelist.seller.id });
        }
    }).filter('pricelistPositionsByProduct', function () {
        return function (product, pricelistPosotions) {
            return _.find(pricelistPosotions, function(position){
                return product.id == position.product.id });
        }
    });
