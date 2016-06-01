(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('StockBufferNodeDialogController', StockBufferNodeDialogController);

    StockBufferNodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockBufferNode', 'User','sharedGeoProperties'];

    function StockBufferNodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockBufferNode, User, sharedGeoProperties) {
        var vm = this;
        vm.stockBufferNode = entity;
        vm.users = User.query();
        
        
        vm.stockBufferNode.longitudePos = sharedGeoProperties.getLongitude(); 

        vm.stockBufferNode.latitudePos = sharedGeoProperties.getLatitude(); 
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:stockBufferNodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stockBufferNode.id !== null) {
                StockBufferNode.update(vm.stockBufferNode, onSaveSuccess, onSaveError);
            } else {
                StockBufferNode.save(vm.stockBufferNode, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
