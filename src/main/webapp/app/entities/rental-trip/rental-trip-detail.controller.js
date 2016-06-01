(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalTripDetailController', RentalTripDetailController);

    RentalTripDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RentalTrip', 'Bicycle', 'MemberMobile', 'RentalBufferNode', 'HourlyRentalCharges'];

    function RentalTripDetailController($scope, $rootScope, $stateParams, entity, RentalTrip, Bicycle, MemberMobile, RentalBufferNode, HourlyRentalCharges) {
        var vm = this;
        vm.rentalTrip = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:rentalTripUpdate', function(event, result) {
            vm.rentalTrip = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
