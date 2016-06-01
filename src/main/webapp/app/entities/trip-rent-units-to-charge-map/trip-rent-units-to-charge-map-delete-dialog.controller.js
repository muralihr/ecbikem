(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TripRentUnitsToChargeMapDeleteController',TripRentUnitsToChargeMapDeleteController);

    TripRentUnitsToChargeMapDeleteController.$inject = ['$uibModalInstance', 'entity', 'TripRentUnitsToChargeMap'];

    function TripRentUnitsToChargeMapDeleteController($uibModalInstance, entity, TripRentUnitsToChargeMap) {
        var vm = this;
        vm.tripRentUnitsToChargeMap = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TripRentUnitsToChargeMap.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
