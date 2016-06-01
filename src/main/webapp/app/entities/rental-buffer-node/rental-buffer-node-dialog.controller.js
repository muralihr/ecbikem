(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalBufferNodeDialogController', RentalBufferNodeDialogController);

    RentalBufferNodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'RentalBufferNode', 'User','sharedGeoProperties'];

    function RentalBufferNodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, RentalBufferNode, User, sharedGeoProperties) {
        var vm = this;
        vm.rentalBufferNode = entity;
        vm.users = User.query();
        
        vm.rentalBufferNode.longitudePos = sharedGeoProperties.getLongitude(); 

        vm.rentalBufferNode.latitudePos = sharedGeoProperties.getLatitude(); 
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:rentalBufferNodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.rentalBufferNode.id !== null) {
                RentalBufferNode.update(vm.rentalBufferNode, onSaveSuccess, onSaveError);
            } else {
                RentalBufferNode.save(vm.rentalBufferNode, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPhotoOfLocation = function ($file, rentalBufferNode) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        rentalBufferNode.photoOfLocation = base64Data;
                        rentalBufferNode.photoOfLocationContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
