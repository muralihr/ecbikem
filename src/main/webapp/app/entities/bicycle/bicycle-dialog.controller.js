(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BicycleDialogController', BicycleDialogController);

    BicycleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bicycle'];

    function BicycleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bicycle) {
        var vm = this;
        vm.bicycle = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:bicycleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.bicycle.id !== null) {
                Bicycle.update(vm.bicycle, onSaveSuccess, onSaveError);
            } else {
                Bicycle.save(vm.bicycle, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateOfPurchase = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
