(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('HourlyRentalChargesDeleteController',HourlyRentalChargesDeleteController);

    HourlyRentalChargesDeleteController.$inject = ['$uibModalInstance', 'entity', 'HourlyRentalCharges'];

    function HourlyRentalChargesDeleteController($uibModalInstance, entity, HourlyRentalCharges) {
        var vm = this;
        vm.hourlyRentalCharges = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            HourlyRentalCharges.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
