(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToRentalNodeMapperDetailController', CycleToRentalNodeMapperDetailController);

    CycleToRentalNodeMapperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CycleToRentalNodeMapper', 'RentalBufferNode', 'Bicycle'];

    function CycleToRentalNodeMapperDetailController($scope, $rootScope, $stateParams, entity, CycleToRentalNodeMapper, RentalBufferNode, Bicycle) {
        var vm = this;
        vm.cycleToRentalNodeMapper = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:cycleToRentalNodeMapperUpdate', function(event, result) {
            vm.cycleToRentalNodeMapper = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
