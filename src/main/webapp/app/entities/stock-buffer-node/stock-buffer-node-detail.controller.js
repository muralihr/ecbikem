(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('StockBufferNodeDetailController', StockBufferNodeDetailController);

    StockBufferNodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StockBufferNode', 'User'];

    function StockBufferNodeDetailController($scope, $rootScope, $stateParams, entity, StockBufferNode, User) {
        var vm = this;
        vm.stockBufferNode = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:stockBufferNodeUpdate', function(event, result) {
            vm.stockBufferNode = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
