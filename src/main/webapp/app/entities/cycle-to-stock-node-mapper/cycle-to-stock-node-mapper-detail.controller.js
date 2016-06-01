(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToStockNodeMapperDetailController', CycleToStockNodeMapperDetailController);

    CycleToStockNodeMapperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CycleToStockNodeMapper', 'StockBufferNode', 'Bicycle'];

    function CycleToStockNodeMapperDetailController($scope, $rootScope, $stateParams, entity, CycleToStockNodeMapper, StockBufferNode, Bicycle) {
        var vm = this;
        vm.cycleToStockNodeMapper = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:cycleToStockNodeMapperUpdate', function(event, result) {
            vm.cycleToStockNodeMapper = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
