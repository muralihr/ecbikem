(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToRentalNodeMapperDialogController', CycleToRentalNodeMapperDialogController);

    CycleToRentalNodeMapperDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CycleToRentalNodeMapper', 'RentalBufferNode', 'Bicycle'];

    function CycleToRentalNodeMapperDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CycleToRentalNodeMapper, RentalBufferNode, Bicycle) {
        var vm = this;
        vm.cycleToRentalNodeMapper = entity;
        vm.rentalbuffernodes = RentalBufferNode.query();
        vm.bicycles = Bicycle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:cycleToRentalNodeMapperUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.cycleToRentalNodeMapper.id !== null) {
                CycleToRentalNodeMapper.update(vm.cycleToRentalNodeMapper, onSaveSuccess, onSaveError);
            } else {
                CycleToRentalNodeMapper.save(vm.cycleToRentalNodeMapper, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
