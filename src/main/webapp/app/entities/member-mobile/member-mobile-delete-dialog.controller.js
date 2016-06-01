(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberMobileDeleteController',MemberMobileDeleteController);

    MemberMobileDeleteController.$inject = ['$uibModalInstance', 'entity', 'MemberMobile'];

    function MemberMobileDeleteController($uibModalInstance, entity, MemberMobile) {
        var vm = this;
        vm.memberMobile = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            MemberMobile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
