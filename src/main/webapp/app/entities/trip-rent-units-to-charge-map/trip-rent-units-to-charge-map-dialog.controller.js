(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TripRentUnitsToChargeMapDialogController', TripRentUnitsToChargeMapDialogController);

    TripRentUnitsToChargeMapDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TripRentUnitsToChargeMap'];

    function TripRentUnitsToChargeMapDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TripRentUnitsToChargeMap) {
        var vm = this;
        vm.tripRentUnitsToChargeMap = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:tripRentUnitsToChargeMapUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tripRentUnitsToChargeMap.id !== null) {
                TripRentUnitsToChargeMap.update(vm.tripRentUnitsToChargeMap, onSaveSuccess, onSaveError);
            } else {
                TripRentUnitsToChargeMap.save(vm.tripRentUnitsToChargeMap, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
