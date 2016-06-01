(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToRentalNodeMapperDeleteController',CycleToRentalNodeMapperDeleteController);

    CycleToRentalNodeMapperDeleteController.$inject = ['$uibModalInstance', 'entity', 'CycleToRentalNodeMapper'];

    function CycleToRentalNodeMapperDeleteController($uibModalInstance, entity, CycleToRentalNodeMapper) {
        var vm = this;
        vm.cycleToRentalNodeMapper = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CycleToRentalNodeMapper.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
