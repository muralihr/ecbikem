(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberBillDeleteController',MemberBillDeleteController);

    MemberBillDeleteController.$inject = ['$uibModalInstance', 'entity', 'MemberBill'];

    function MemberBillDeleteController($uibModalInstance, entity, MemberBill) {
        var vm = this;
        vm.memberBill = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            MemberBill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
