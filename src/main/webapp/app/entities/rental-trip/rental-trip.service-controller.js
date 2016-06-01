(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('RentalTripServiceController', RentalTripController);

    RentalTripController.$inject = ['$scope', '$filter','$state', 'RentalTrip', 'RentalTripSearch', 'ParseLinks', 'AlertService',   'RentalBufferNode', 'Bicycle','MemberMobile', '$http'];

    function RentalTripController ($scope,$filter, $state, RentalTrip, RentalTripSearch, ParseLinks, AlertService,  RentalBufferNode, Bicycle,MemberMobile, $http) {
       

        ///ADDED 
        $scope.loadAllBicyclesFromNode = function() {

            var path = $scope.rentalTrip.startNode.id;
            var res = $http.get('api/bicycles/bicyclesatnode/' + path, {});
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
			var res = $http.post('api/rental-trips/starttrip', dataObj);
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

        $scope.rentalbuffernodes = RentalBufferNode.query();
        
      
    
        $scope.stopTrip  = function() {
        	var dataObj = {
		 		    "bicycleId":  $scope.rentalCycle, 
			        "rentalMemberId":  $scope.stoptripmember.id,
			        "rentalStartTime": $scope.rentStartTime,
			        "startNodeId":     $scope.startNode ,
			        "stopNodeId":      $scope.stopNode.id,
			        "rentalStopTime":  $scope.rentStopTime
        	};	
			var res = $http.post('api/rental-trips/stoptrip', dataObj);
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
            var url = 'api/rental-trips/isMemberValid/' + $scope.tripmember.id;
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
            var url = 'api/rental-trips/findTripByMember/' + $scope.stoptripmember.id;
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
        
        $scope.onChangeDateFindCompletedTrips  = function() {
        	
            console.log("onChangeDateFindCompletedTrips");
           	
          	var dateFormat = 'yyyy-MM-dd';
            var fromDate = $filter('date')($scope.fromDate, dateFormat);
            var toDate = $filter('date')($scope.toDate, dateFormat);
    
           	var dataObj = {
   		 		     
   			        "fromDate": fromDate,			      
   			        "toDate":  toDate
           	};	
   			var res = $http.put('api/rental-trips/findCompleteTripsByDates', dataObj);
   			res.success(function(data, status, headers, config) {
   			$scope.alltrips = data;
    			});
   			res.error(function(data, status, headers, config) {
   		 
   				 alert( "failure message: " + JSON.stringify({data: data}));
   			});	
           }
           // Date picker configuration
           $scope.today = function () {
               // Today + 1 day - needed if the current day must be included
               var today = new Date();
               $scope.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
           };

           $scope.previousMonth = function () {
               var fromDate = new Date();
               if (fromDate.getMonth() === 0) {
                   fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
               } else {
                   fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
               }

               $scope.fromDate = fromDate;
           };

           $scope.today();
           $scope.previousMonth();
          // $scope.onChangeDate();
           
           $scope.items = ['CALLED ONCE', 'CALLED TWICE', 'CALLED THRICE','BLOCK MEMBER', 'ALLOW MEMBER'];

           $scope.animationsEnabled = true;
           
           
           


    }
})();
