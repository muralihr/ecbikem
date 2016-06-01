(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('StockBufferNodeController', StockBufferNodeController);
    
    StockBufferNodeController.$inject = ['$scope', '$state', 'StockBufferNode', 'StockBufferNodeSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','sharedGeoProperties'];

    function StockBufferNodeController ($scope, $state, StockBufferNode, StockBufferNodeSearch, ParseLinks, AlertService, pagingParams, paginationConstants,sharedGeoProperties) {
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
        vm.loadAll();

        function loadAll () {
            if (pagingParams.search) {
                StockBufferNodeSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                StockBufferNode.query({
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
                vm.stockBufferNodes = data;
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
        
        ////
        

		
		angular.extend($scope, {
            center: {
                lat:  11.93252413,
                lng:  79.82084512, 
                
                zoom: 15
            },
            
            markers: {
            	 centerMarker: {
            		 lat:   12.967088,
                     lng:  77.6323127,
                     
                     message: "Click on the map to move the marker to a Specific Location!",
                     focus: true,
                     draggable: true
                 }
            },
            
            events: {
		        markers: {
		            enable: ['click'],
		            logic: 'emit'
		        }
		    },
            tiles: {
                name: 'Mapbox Park',
                url: 'http://api.tiles.mapbox.com/v4/{mapid}/{z}/{x}/{y}.png?access_token={apikey}',
                type: 'xyz',
                options: {
                    apikey: 'pk.eyJ1IjoibXVyYWxpaHI3NyIsImEiOiJjaWo5c2tqZjYwMDNtdXhseGFqeHlsZnQ4In0.W_DdV-qM8lNZzacVotHDEA',
                    mapid: 'mapbox.streets'
                }
            },
            geojson: {}

        });
		
		
		 $scope.$on('leafletDirectiveMap.click', function(event, args){
    		 console.log("click on leafletDirectiveMap"+event);
    		 
    		 var lat = args.leafletEvent.latlng.lat; //has to be set above
    		 var lng = args.leafletEvent.latlng.lng; //has to be set above    		 
    		 $scope.markers.centerMarker.lat = lat;
    		 $scope.markers.centerMarker.lng = lng;    		 
    		 $scope.lat2 = lat;
             $scope.lng2 = lng;
             sharedGeoProperties.setLongitude(lng );
             sharedGeoProperties.setLatitude(lat );
		 });
		 ///leaf let end
        
        ////

    }
})();
