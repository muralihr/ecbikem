(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalBufferNodeDeleteController',RentalBufferNodeDeleteController);

    RentalBufferNodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'RentalBufferNode'];

    function RentalBufferNodeDeleteController($uibModalInstance, entity, RentalBufferNode) {
        var vm = this;
        vm.rentalBufferNode = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RentalBufferNode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
