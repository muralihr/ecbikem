(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberMobileDetailController', MemberMobileDetailController);

    MemberMobileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'MemberMobile'];

    function MemberMobileDetailController($scope, $rootScope, $stateParams, DataUtils, entity, MemberMobile) {
        var vm = this;
        vm.memberMobile = entity;
        
        var unsubscribe = $rootScope.$on('ecbikeApp:memberMobileUpdate', function(event, result) {
            vm.memberMobile = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
