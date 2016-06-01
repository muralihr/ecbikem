(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToCustomerMapperDetailController', CycleToCustomerMapperDetailController);

    CycleToCustomerMapperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CycleToCustomerMapper', 'MemberMobile', 'Bicycle'];

    function CycleToCustomerMapperDetailController($scope, $rootScope, $stateParams, entity, CycleToCustomerMapper, MemberMobile, Bicycle) {
        var vm = this;
        vm.cycleToCustomerMapper = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:cycleToCustomerMapperUpdate', function(event, result) {
            vm.cycleToCustomerMapper = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
