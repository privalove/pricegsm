function AppCtrl() {

}

function IndexCtrl($scope, indexResource) {
    if (indexResource.ok) {
        angular.extend($scope, indexResource.payload);

        $scope.gridPriceOptions = {
            data: $scope.prices,
            groups: ['vendor'],
            multiSelect: false,
            plugins: [new ngGridFlexibleHeightPlugin()],
            columnDefs:[
                {field:'vendor', displayName:'Производитель'},
                {field:'product', displayName:'Наименование'},
                {field:'color', displayName:'Цвет'},
                {field:'retail', displayName:'Розница'},
                {field:'opt', displayName:'Опт'},
                {field:'world', displayName:'Мир'}
            ]
        }

    }
}

IndexCtrl.resolve = {
    indexResource: function (IndexResource) {
        return IndexResource.get().$promise;
    }
};

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
