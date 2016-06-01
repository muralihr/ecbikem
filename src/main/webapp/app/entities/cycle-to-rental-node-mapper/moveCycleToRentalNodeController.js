angular.module('ecbikeApp'  ).controller('MoveCycleToRentalNodeController', function($scope, 	CycleToStockNodeMapper, CycleToRentalNodeMapper,StockBufferNode,RentalBufferNode, Bicycle,$http) {
         

    ///ADDEDS

    $scope.page = 1;
    $scope.cycleToRentalNodeMapperObj = new Object();
    $scope.cyclesAtStock = [];
    $scope.cycleToStockNodeMapperBuff = [];
    
    
    $scope.loadAllStock = function() {
        StockBufferNode.query(function(result) {
            $scope.stockBufferNodes = result;
        });
    }; 

    $scope.loadAllNode = function() {
        RentalBufferNode.query(function(result) {
            $scope.rentalBufferNodes = result;
        });
    };

    $scope.loadAllNode();
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
			$scope.availableclients = data;
			
			angular.forEach($scope.stockBicycles , function(item) {
	           console.log("bicycle "+item.tagId);
	        });
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
    

  ///ADDED 
    
    ///////////////////ADDED 
    $scope.moveItem = function(item, from, to) {



        console.log('Move item   Item: ' + item + ' From:: ' + from + ' To:: ' + to);
        //Here from is returned as blank and to as undefined

        var idx = from.indexOf(item);
        if (idx != -1) {
            from.splice(idx, 1);
            to.push(item);
        }
    };
    $scope.moveAll = function(from, to) {

        console.log('Move all  From:: ' + from + ' To:: ' + to);
        //Here from is returned as blank and to as undefined

        angular.forEach(from, function(item) {
            to.push(item);
        });
        from.length = 0;
    };

    $scope.selectedclients = [];


    $scope.selectAction = function(selectedclients) {
        console.log("selected stock" + $scope.selectedStock);
        console.log("selectedclients" + $scope.selectedclients);

        $scope.loadAllBicyclesFromStock();
    };




    $scope.selectNodeAction = function(selectedclients) {
        console.log("selected selectedNode" + $scope.selectedNode);
        console.log("selectedclients" + $scope.selectedclients);
        //$scope.loadAllCurrentCyclesFromNode();
    };
    Array.prototype.remove = function(value) {
        if (this.indexOf(value) !== -1) {
            this.splice(this.indexOf(value), 1);
            return true;
        } else {
            return false;
        };
    }
    var onSaveFinished2 = function(result) {
        $scope.$emit('ecbikeApp:moveCyclesToSelectedNode', result);


    };

    var onSaveFinished1 = function(result) {
        $scope.$emit('ecbikeApp:moveCyclesToSelectedNode', result);
    };

    var onSaveFinishedBicycle = function(result) {
        $scope.$emit('ecbikeApp:Bicycle Updated', result);
    };



    $scope.moveCyclesToSelectedNode = function(array, stockBufferNode, rentalBufferNode) {

        console.log("cycle" + array + "moving to :  " + rentalBufferNode - 1);
        angular.forEach(array, function(item) {

                var dataObj = {
                    "bicycleId": item.tagId,
                    "rentalNodeId": $scope.rentalBufferNodes[rentalBufferNode - 1].id,
                    "stockNodeId": $scope.stockBufferNodes[stockBufferNode - 1].id
                };
                var res = $http.post('/api/cycle-to-stock-node-mappers/moveCycleToNode', dataObj);
                res.success(function(data, status, headers, config) {
                    $scope.message = data;


                });
                res.error(function(data, status, headers, config) {
                    alert("failure message: " + JSON.stringify({
                        data: data
                    }));
                });
                array.remove(item);


            }

        );


    };
    $scope.selectedclients = [];                                

    $scope.availableclients = [
       
    ];
  });  

  