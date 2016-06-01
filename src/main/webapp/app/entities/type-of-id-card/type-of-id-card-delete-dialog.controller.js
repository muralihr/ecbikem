(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TypeOfIDCardDeleteController',TypeOfIDCardDeleteController);

    TypeOfIDCardDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeOfIDCard'];

    function TypeOfIDCardDeleteController($uibModalInstance, entity, TypeOfIDCard) {
        var vm = this;
        vm.typeOfIDCard = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TypeOfIDCard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
