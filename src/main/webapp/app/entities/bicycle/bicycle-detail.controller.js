(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BicycleDetailController', BicycleDetailController);

    BicycleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Bicycle'];

    function BicycleDetailController($scope, $rootScope, $stateParams, entity, Bicycle) {
        var vm = this;
        vm.bicycle = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:bicycleUpdate', function(event, result) {
            vm.bicycle = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
