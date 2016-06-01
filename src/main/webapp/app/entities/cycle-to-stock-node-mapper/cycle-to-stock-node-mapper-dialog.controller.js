(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToStockNodeMapperDialogController', CycleToStockNodeMapperDialogController);

    CycleToStockNodeMapperDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CycleToStockNodeMapper', 'StockBufferNode', 'Bicycle'];

    function CycleToStockNodeMapperDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CycleToStockNodeMapper, StockBufferNode, Bicycle) {
        var vm = this;
        vm.cycleToStockNodeMapper = entity;
        vm.stockbuffernodes = StockBufferNode.query();
        vm.bicycles = Bicycle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:cycleToStockNodeMapperUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.cycleToStockNodeMapper.id !== null) {
                CycleToStockNodeMapper.update(vm.cycleToStockNodeMapper, onSaveSuccess, onSaveError);
            } else {
                CycleToStockNodeMapper.save(vm.cycleToStockNodeMapper, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
