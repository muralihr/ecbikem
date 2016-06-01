(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToCustomerMapperDeleteController',CycleToCustomerMapperDeleteController);

    CycleToCustomerMapperDeleteController.$inject = ['$uibModalInstance', 'entity', 'CycleToCustomerMapper'];

    function CycleToCustomerMapperDeleteController($uibModalInstance, entity, CycleToCustomerMapper) {
        var vm = this;
        vm.cycleToCustomerMapper = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CycleToCustomerMapper.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
