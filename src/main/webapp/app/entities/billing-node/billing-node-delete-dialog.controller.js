(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('BillingNodeDeleteController',BillingNodeDeleteController);

    BillingNodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'BillingNode'];

    function BillingNodeDeleteController($uibModalInstance, entity, BillingNode) {
        var vm = this;
        vm.billingNode = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            BillingNode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
