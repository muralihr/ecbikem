(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('StockBufferNodeDeleteController',StockBufferNodeDeleteController);

    StockBufferNodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockBufferNode'];

    function StockBufferNodeDeleteController($uibModalInstance, entity, StockBufferNode) {
        var vm = this;
        vm.stockBufferNode = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            StockBufferNode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
