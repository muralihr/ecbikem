(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalTripDeleteController',RentalTripDeleteController);

    RentalTripDeleteController.$inject = ['$uibModalInstance', 'entity', 'RentalTrip'];

    function RentalTripDeleteController($uibModalInstance, entity, RentalTrip) {
        var vm = this;
        vm.rentalTrip = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RentalTrip.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
