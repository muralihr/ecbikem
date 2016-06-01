(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberBillDetailController', MemberBillDetailController);

    MemberBillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MemberBill', 'BillingNode', 'MemberMobile', 'TripRentUnitsToChargeMap'];

    function MemberBillDetailController($scope, $rootScope, $stateParams, entity, MemberBill, BillingNode, MemberMobile, TripRentUnitsToChargeMap) {
        var vm = this;
        vm.memberBill = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:memberBillUpdate', function(event, result) {
            vm.memberBill = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
