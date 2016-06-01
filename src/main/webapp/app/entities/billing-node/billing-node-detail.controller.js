(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BillingNodeDetailController', BillingNodeDetailController);

    BillingNodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'BillingNode', 'User'];

    function BillingNodeDetailController($scope, $rootScope, $stateParams, entity, BillingNode, User) {
        var vm = this;
        vm.billingNode = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:billingNodeUpdate', function(event, result) {
            vm.billingNode = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
