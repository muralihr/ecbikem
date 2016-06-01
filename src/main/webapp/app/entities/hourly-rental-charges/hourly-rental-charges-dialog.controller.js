(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('HourlyRentalChargesDialogController', HourlyRentalChargesDialogController);

    HourlyRentalChargesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HourlyRentalCharges'];

    function HourlyRentalChargesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HourlyRentalCharges) {
        var vm = this;
        vm.hourlyRentalCharges = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:hourlyRentalChargesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.hourlyRentalCharges.id !== null) {
                HourlyRentalCharges.update(vm.hourlyRentalCharges, onSaveSuccess, onSaveError);
            } else {
                HourlyRentalCharges.save(vm.hourlyRentalCharges, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
