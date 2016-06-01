(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('TypeOfIDCardDetailController', TypeOfIDCardDetailController);

    TypeOfIDCardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TypeOfIDCard'];

    function TypeOfIDCardDetailController($scope, $rootScope, $stateParams, entity, TypeOfIDCard) {
        var vm = this;
        vm.typeOfIDCard = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:typeOfIDCardUpdate', function(event, result) {
            vm.typeOfIDCard = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
