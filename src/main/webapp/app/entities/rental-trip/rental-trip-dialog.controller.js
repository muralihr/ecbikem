(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalTripDialogController', RentalTripDialogController);

    RentalTripDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RentalTrip', 'Bicycle', 'MemberMobile', 'RentalBufferNode', 'HourlyRentalCharges'];

    function RentalTripDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RentalTrip, Bicycle, MemberMobile, RentalBufferNode, HourlyRentalCharges) {
        var vm = this;
        vm.rentalTrip = entity;
        vm.bicycles = Bicycle.query();
        vm.membermobiles = MemberMobile.query();
        vm.rentalbuffernodes = RentalBufferNode.query();
        vm.hourlyrentalcharges = HourlyRentalCharges.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:rentalTripUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.rentalTrip.id !== null) {
                RentalTrip.update(vm.rentalTrip, onSaveSuccess, onSaveError);
            } else {
                RentalTrip.save(vm.rentalTrip, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.rentStartTime = false;
        vm.datePickerOpenStatus.rentStopTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
