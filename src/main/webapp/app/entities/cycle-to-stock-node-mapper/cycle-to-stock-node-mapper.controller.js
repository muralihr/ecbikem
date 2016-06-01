(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('CycleToStockNodeMapperController', CycleToStockNodeMapperController);

    CycleToStockNodeMapperController.$inject = ['$scope', '$state', 'CycleToStockNodeMapper', 'CycleToStockNodeMapperSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function CycleToStockNodeMapperController ($scope, $state, CycleToStockNodeMapper, CycleToStockNodeMapperSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
       // vm.loadAll();
      //  vm.loadAllStock();
        
        
        //        
        vm.cycleToStockNodeMappers = [];
        vm.page = 1;
        vm.cycleToStockNodeMapperObj = new Object() ;
        vm.untaggedcycles = [];
        vm.cycleToStockNodeMapperBuff = [];
        //

        function loadAll () {
            if (pagingParams.search) {
                CycleToStockNodeMapperSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                CycleToStockNodeMapper.query({
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.cycleToStockNodeMappers = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

        
        ////move cycle to stock functions
        
        ////
		 function loadAllStock()  {
            StockBufferNode.query(function(result) {
               vm.stockBufferNodes = result;
            });
        };
        
        ///////////////////ADDED 
        $scope.moveItem = function(item, from, to) {

  		  
        	
        	        console.log('Move item   Item: '+item+' From:: '+from+' To:: '+to);
        	        //Here from is returned as blank and to as undefined

        	        var idx=from.indexOf(item);
        	        if (idx != -1) {
        	            from.splice(idx, 1);
        	            to.push(item);      
        	        }
        	    };
       $scope.moveAll = function(from, to) {

        	        console.log('Move all  From:: '+from+' To:: '+to);
        	        //Here from is returned as blank and to as undefined

        	        angular.forEach(from, function(item) {
        	            to.push(item);
        	        });
        	        from.length = 0;
        	    };                

    }
})();
