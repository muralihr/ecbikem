(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
    	
    	$scope.steps = [
    	                {
    	                	
    	                	    
    	                    templateUrl: 'app/home/section2-1.html',
    	                    title: 'Introduction'
    	                },
    	                {
    	                	templateUrl: 'app/home/section2-2.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                },
    	                {
    	                	templateUrl: 'app/home/section2-3.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                },
    	                {
    	                	templateUrl: 'app/home/section2-4.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                }
    	                
    	                ];
    	
    	$scope.steps2 = [
    	                {
    	                	
    	                	    
    	                    templateUrl: 'app/home/section6-1.html',
    	                    title: 'Introduction'
    	                },
    	                {
    	                	templateUrl: 'app/home/section6-2.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                },
    	                {
    	                	templateUrl: 'app/home/section6-3.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                },
    	                {
    	                	templateUrl: 'app/home/section6-4.html',
    	                    
    	                    hasForm: false,
    	                    title: 'Update validity'
    	                }
    	                
    	                ];
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
