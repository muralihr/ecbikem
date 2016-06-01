(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BillingNodeDialogController', BillingNodeDialogController);

    BillingNodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BillingNode', 'User','sharedGeoProperties'];

    function BillingNodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BillingNode, User,sharedGeoProperties) {
        var vm = this;
        vm.billingNode = entity;
        vm.users = User.query();
        vm.billingNode.longitudePos = sharedGeoProperties.getLongitude();
        vm.billingNode.latitudePos = sharedGeoProperties.getLatitude();
        

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:billingNodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.billingNode.id !== null) {
                BillingNode.update(vm.billingNode, onSaveSuccess, onSaveError);
            } else {
                BillingNode.save(vm.billingNode, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
