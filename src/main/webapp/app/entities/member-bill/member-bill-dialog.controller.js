(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberBillDialogController', MemberBillDialogController);

    MemberBillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberBill', 'BillingNode', 'MemberMobile', 'TripRentUnitsToChargeMap'];

    function MemberBillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MemberBill, BillingNode, MemberMobile, TripRentUnitsToChargeMap) {
        var vm = this;
        vm.memberBill = entity;
        vm.billingnodes = BillingNode.query();
        vm.membermobiles = MemberMobile.query();
        vm.triprentunitstochargemaps = TripRentUnitsToChargeMap.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:memberBillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.memberBill.id !== null) {
                MemberBill.update(vm.memberBill, onSaveSuccess, onSaveError);
            } else {
                MemberBill.save(vm.memberBill, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateOfPayment = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
