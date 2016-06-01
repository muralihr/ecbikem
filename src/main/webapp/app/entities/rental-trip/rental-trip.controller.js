(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalTripController', RentalTripController);

    RentalTripController.$inject = ['$scope', '$state', 'RentalTrip', 'RentalTripSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'RentalBufferNode', 'Bicycle','MemberMobile', '$http'];

    function RentalTripController ($scope, $state, RentalTrip, RentalTripSearch, ParseLinks, AlertService, pagingParams, paginationConstants,RentalBufferNode, Bicycle,MemberMobile, $http) {
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
                RentalTripSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                RentalTrip.query({
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
                vm.rentalTrips = data;
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
        
        

        ///ADDED 
        $scope.loadAllBicyclesFromNode = function() {

            var path = $scope.rentalTrip.startNode.id;
            var res = $http.get('api/bicyclesatnode/' + path, {});
            //var res = $http.get('api/bicyclesatstock/ + stockBufferNode+"'" );
            res.success(function(data, status, headers, config) {
                $scope.bicycles = data;
                console.log("$scope.nodeBicycles" + $scope.bicycles);

                angular.forEach($scope.bicycles, function(item) {
                    console.log("bicycle " + item.tagId);
                });
            });
            res.error(function(data, status, headers, config) {
                alert("failure message: " + JSON.stringify({
                    data: data
                }));
            });
        };
        
        
        $scope.startTrip  = function() {
        	var dataObj = {
		 		    "bicycleId": $scope.bicycles[$scope.rentalTrip.rentedCycle.id-1].tagId, 
			        "rentalMemberId": $scope.tripmember.id,
			        "rentalStartTime": $scope.rentalTrip.rentStartTime,
			        "startNodeId":$scope.rentalTrip.startNode.id 
        	};	
			var res = $http.post('api/starttrip', dataObj);
			res.success(function(data, status, headers, config) {
			$scope.message = data;
			 

            console.log("starttrip-status" + data.status);
            console.log("starttrip-message" + data.message);
            console.log("starttrip-code" + data.code);
            $scope.memberValid = false;

			 
			});
			res.error(function(data, status, headers, config) {
				 $scope.memberValid = false;
				 alert( "failure message: " + JSON.stringify({data: data}));
			});	

        }
        
        $scope.getMember = function (id) {
            MemberMobile.get({id: id}, function(result) {
                $scope.memberMobile = result;
                
            });
        };
        
        //stopTrip()
        
        /*
         * 
         *  
    	 
       
       
         "stopNodeId":1,
        "rentalStopTime":"2011-08-21T19:02:52.249Z",
}

         */
    
        $scope.stopTrip  = function() {
        	var dataObj = {
		 		    "bicycleId":  $scope.rentalCycle, 
			        "rentalMemberId":  $scope.stoptripmember.id,
			        "rentalStartTime": $scope.rentStartTime,
			        "startNodeId":     $scope.startNode ,
			        "stopNodeId":      $scope.stopNode.id,
			        "rentalStopTime":  $scope.rentStopTime
        	};	
			var res = $http.post('api/stoptrip', dataObj);
			res.success(function(data, status, headers, config) {
			$scope.message = data;
			 

            console.log("stopTrip-status" + data.status);
            console.log("stopTrip-message" + data.message);
            console.log("stopTrip-code" + data.code);
            $scope.memberValid = false;

			 
			});
			res.error(function(data, status, headers, config) {
				 $scope.memberValid = false;
				 alert( "failure message: " + JSON.stringify({data: data}));
			});	

        }

		$scope.checkAndDisplay = function() {
            alert("cheking" + $scope.tripmember.id);
            //var dataObj = 'memberId=' +$scope.tripmember.id ;
            var url = 'api/isMemberValid/' + $scope.tripmember.id;
            var res = $http.post(url);
            res.success(function(data, status, headers, config) {
                $scope.message = data;
                
                console.log("isMemberValid" + data);

                console.log("isMemberValid-status" + data.status);
                console.log("isMemberValid-message" + data.message);
                console.log("isMemberValid-code" + data.code);

                if (data.code == 200) {
                    $scope.memberValid = true;
                    $scope.getMember($scope.tripmember.id);

                }

            });
            res.error(function(data, status, headers, config) {
                alert("failure message: " + JSON.stringify({
                    data: data
                }));
            });

        };
        //stoptripmember.idfindTripToStop
        $scope.findTripToStop = function() {
            alert("cheking" + $scope.stoptripmember.id);  
            //var dataObj = 'memberId=' +$scope.tripmember.id ;
            var url = 'api/findTripByMember/' + $scope.stoptripmember.id;
            var res = $http.post(url);
            res.success(function(data, status, headers, config) {
                $scope.message = data;
                
                console.log("findTripByMember" + data);

                console.log("findTripToStop-status" + data.status);
                console.log("findTripToStop-message" + data.message);
                console.log("findTripToStop-code" + data.code); 

                if (data.code == 200) {
                    $scope.memberValid = true;
                    $scope.getMember($scope.stoptripmember.id);
                    console.log("findTripToStop-code" + data.tripMapper.bicycleId);

                    console.log("findTripToStop-code" + data.tripMapper.startNodeId);

                    console.log("findTripToStop-code" + data.tripMapper.rentalStartTime);
                   
                     $scope.rentStartTime = data.tripMapper.rentalStartTime;
                     $scope.rentalCycle =	 data.tripMapper.bicycleId;
                     $scope.startNode  =  data.tripMapper.startNodeId;

                }

            });
            res.error(function(data, status, headers, config) {
                alert("failure message: " + JSON.stringify({
                    data: data
                }));
            });

        }


    }
})();
