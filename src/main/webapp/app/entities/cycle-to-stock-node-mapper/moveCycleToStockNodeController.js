angular.module('ecbikeApp'  ).controller('MoveCycleToStockNodeController', function($scope, 	CycleToStockNodeMapper, StockBufferNode, Bicycle,$http) {
         
	$scope.cycleToStockNodeMappers = [];
    $scope.page = 1;
    $scope.cycleToStockNodeMapperObj = new Object() ;
    $scope.untaggedcycles = [];
   $scope.cycleToStockNodeMapperBuff = [];
	     
	$scope.loadAllBicycles = function() {
        Bicycle.query(function(result) {
           $scope.bicycles = result;
		   $scope.availableclients = result;
        });
    };
   
    $scope.loadAllBicyclesFromStock = function() {
        
    	var path = $scope.selectedStock;
    	var res = $http.get('api/bicycles/bicyclesatstock/' + path, {});
    	//var res = $http.get('api/bicyclesatstock/ + stockBufferNode+"'" );
		res.success(function(data, status, headers, config) {
			$scope.stockBicycles = data;
			console.log("$scope.stockBicycles"+$scope.stockBicycles);
			
			angular.forEach($scope.stockBicycles , function(item) {
	           console.log("bicycle "+item.tagId);
	        });
		});
		res.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});		
    };
    
    $scope.loadAllUntaggedCycles = function() {
        
		var res = $http.get('api/bicycles/untaggedbicycles' );
		res.success(function(data, status, headers, config) {
			$scope.untaggedcycles = data;
			 $scope.bicycles = data;
			   $scope.availableclients = data;
			console.log("$scope.untaggedcycles" + $scope.untaggedcycles);
		});
		
		res.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});		
    };
    
    

	$scope.loadAllStock = function() {
        StockBufferNode.query(function(result) {
           $scope.stockBufferNodes = result;
        });
    };
    $scope.loadAllStock();
    $scope.loadAllUntaggedCycles();

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
    
    $scope.selectAction = function(selectedclients) {
    	
	    console.log("selected stock"+$scope.selectedStock); 
		console.log("selectedclients" +$scope.selectedclients);
		// $scope.loadAllBicyclesFromStock();	
		
	};
		
	
	  var onSaveFinished2 = function (result) {
          $scope.$emit('ecbikeApp:moveCyclesToSelectedStock', result);
          
         
      };
      
	   var onSaveFinishedBicycle = function (result) {
         $scope.$emit('ecbikeApp:Bicycle Updated', result);
         
        
     };
     
     Array.prototype.remove = function(value) {
		    if (this.indexOf(value)!==-1) {
		       this.splice(this.indexOf(value), 1);
		       return true;
		   } else {
		      return false;
		   };
		} 
     
      
	$scope.moveCyclesToSelectedStock = function (array,stockBufferNode) {
        
		 
		   console.log("cycle"+ array + "moving to :  "+stockBufferNode-1 ); 
		   
		  angular.forEach(array, function(item) {
       //  console.log(item.tagId);
		   
		  $scope.cycleToStockNodeMapperObj.movedCycle = item;
		  $scope.cycleToStockNodeMapperObj.movedCycle.moveStatus = 1;        			  
		  $scope.cycleToStockNodeMapperObj.nodeDest = $scope.stockBufferNodes[stockBufferNode-1];
		  CycleToStockNodeMapper.save($scope.cycleToStockNodeMapperObj,onSaveFinished2 );
		  Bicycle.update( $scope.cycleToStockNodeMapperObj.movedCycle, onSaveFinishedBicycle);
		  array.remove(item);
		  
       }
		
		);
		    
        
  };	
    $scope.selectedclients = [];                                

    $scope.availableclients = [
       
    ];
  });  

  