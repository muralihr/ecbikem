(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalBufferNodeDetailController', RentalBufferNodeDetailController);

    RentalBufferNodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'RentalBufferNode', 'User'];

    function RentalBufferNodeDetailController($scope, $rootScope, $stateParams, DataUtils, entity, RentalBufferNode, User) {
        var vm = this;
        vm.rentalBufferNode = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:rentalBufferNodeUpdate', function(event, result) {
            vm.rentalBufferNode = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
