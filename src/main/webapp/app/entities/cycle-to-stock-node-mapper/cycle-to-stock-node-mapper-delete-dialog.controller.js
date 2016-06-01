(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToStockNodeMapperDeleteController',CycleToStockNodeMapperDeleteController);

    CycleToStockNodeMapperDeleteController.$inject = ['$uibModalInstance', 'entity', 'CycleToStockNodeMapper'];

    function CycleToStockNodeMapperDeleteController($uibModalInstance, entity, CycleToStockNodeMapper) {
        var vm = this;
        vm.cycleToStockNodeMapper = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CycleToStockNodeMapper.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
