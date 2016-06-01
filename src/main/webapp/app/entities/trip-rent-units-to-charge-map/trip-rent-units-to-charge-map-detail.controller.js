(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TripRentUnitsToChargeMapDetailController', TripRentUnitsToChargeMapDetailController);

    TripRentUnitsToChargeMapDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TripRentUnitsToChargeMap'];

    function TripRentUnitsToChargeMapDetailController($scope, $rootScope, $stateParams, entity, TripRentUnitsToChargeMap) {
        var vm = this;
        vm.tripRentUnitsToChargeMap = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:tripRentUnitsToChargeMapUpdate', function(event, result) {
            vm.tripRentUnitsToChargeMap = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
