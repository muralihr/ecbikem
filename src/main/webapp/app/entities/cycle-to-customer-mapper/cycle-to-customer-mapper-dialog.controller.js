(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToCustomerMapperDialogController', CycleToCustomerMapperDialogController);

    CycleToCustomerMapperDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CycleToCustomerMapper', 'MemberMobile', 'Bicycle'];

    function CycleToCustomerMapperDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CycleToCustomerMapper, MemberMobile, Bicycle) {
        var vm = this;
        vm.cycleToCustomerMapper = entity;
        vm.membermobiles = MemberMobile.query();
        vm.bicycles = Bicycle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:cycleToCustomerMapperUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.cycleToCustomerMapper.id !== null) {
                CycleToCustomerMapper.update(vm.cycleToCustomerMapper, onSaveSuccess, onSaveError);
            } else {
                CycleToCustomerMapper.save(vm.cycleToCustomerMapper, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
