(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BicycleDeleteController',BicycleDeleteController);

    BicycleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bicycle'];

    function BicycleDeleteController($uibModalInstance, entity, Bicycle) {
        var vm = this;
        vm.bicycle = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Bicycle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
