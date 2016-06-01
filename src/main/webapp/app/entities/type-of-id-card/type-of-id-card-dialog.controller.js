(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TypeOfIDCardDialogController', TypeOfIDCardDialogController);

    TypeOfIDCardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeOfIDCard'];

    function TypeOfIDCardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeOfIDCard) {
        var vm = this;
        vm.typeOfIDCard = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:typeOfIDCardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.typeOfIDCard.id !== null) {
                TypeOfIDCard.update(vm.typeOfIDCard, onSaveSuccess, onSaveError);
            } else {
                TypeOfIDCard.save(vm.typeOfIDCard, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
