(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('HourlyRentalChargesDetailController', HourlyRentalChargesDetailController);

    HourlyRentalChargesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'HourlyRentalCharges'];

    function HourlyRentalChargesDetailController($scope, $rootScope, $stateParams, entity, HourlyRentalCharges) {
        var vm = this;
        vm.hourlyRentalCharges = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:hourlyRentalChargesUpdate', function(event, result) {
            vm.hourlyRentalCharges = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
