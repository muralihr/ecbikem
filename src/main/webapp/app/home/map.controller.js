'use strict';

angular.module('ecbikeApp')
    .controller('NodeMapController',['$scope', '$http','$uibModal', '$location', 'Principal','leafletData', 'RentalBufferNode',
                                  function ($scope,$http,$uibModal, $location, Principal,  leafletData ,RentalBufferNode) {
       
    	
    	
    	Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;         
                 
            
        });
  
    	///ANGULAR LEAFLET 
    	$scope.map = null;
    	
    	leafletData.getMap("map").then(function(map){
            $scope.map = map;
        });
    	 angular.extend($scope, {
             center: {
            	 lat:  12.9189065,
                 lng:  77.4755859,
                 zoom: 14
             },
             aurovillecenter: { //12.007127, 79.810771
            	 lat:  12.9189065,
                 lng:  77.4755859,
                 zoom: 14 
             },
             towncenter: {
            	 lat:  12.9189065,
                 lng:  77.4755859,
                 zoom: 14
             },
             heritagecenter: { //, 
                 lat:  12.9189065,
                 lng:  77.4755859,
                 zoom: 14
             },
             bahourcenter: { //(11.803506, 79.738941)
            	 lat:  12.9189065,
                 lng:  77.4755859,
                 zoom: 14
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
                     mapid: 'mapbox.pirates'
                 }
             },
             geojson:
             {
            	  
            	 
             }

         });
    	 
     
    	 
    	 

         //$http.get("https://a.tiles.mapbox.com/v4/feelcreative.llm8dpdk/features.json?access_token=pk.eyJ1IjoibXVyYWxpaHI3NyIsImEiOiJjaWo5c2tqZjYwMDNtdXhseGFqeHlsZnQ4In0.W_DdV-qM8lNZzacVotHDEA").success(function(data) {
    	 $http.get("api/rental-buffer-nodes/nodemapp").success(function(data) {
           //  $scope.geojson.data = data;
              console.log("geojson data "+data);
    		 
    		 angular.extend($scope, {
    	            geojson: {
    	                data: data,
    	                resetStyleOnMouseout: true,
    	                style: {
    	                    fillColor: "green",
    	                    weight: 2,
    	                    opacity: 1,
    	                    color: 'white',
    	                    dashArray: '3',
    	                    fillOpacity: 0.7
    	                }
    	            }
    	        });
             
             
             //show window 
             
             
         });
    	//LEAFLET 
    	 
    	 //leaf let variables 
    	 
    	 $scope.markerLatSelected;
         $scope.markerLngSelected;
         $scope.titleSelected ="dddd";
         $scope.address ="dddd";
         $scope.cyclecount;
         $scope.cycles;
         $scope.showmsg=false;
         
    
         ///
         
   
         $scope.$on("leafletDirectiveGeoJson.mouseover", function(ev, args) {
              
             $scope.titleSelected  =  args.model.properties.title;;
             console.log("$scope.titleSelected"+$scope.titleSelected);
             $scope.address = args.model.properties.address;;
            // $scope.cyclecount  = args.model.properties.cycle-count; 
            // console.log("  $scope.cyclecount"+  $scope.cyclecount);
             $scope.cycles=args.model.properties.cycles;
             $scope.showmsg=true;
         });
         
         
         $scope.$on("leafletDirectiveGeoJson.mouseout", function(ev, args) {
        	 $scope.showmsg=false;
            
         });
         
        
    	 ///
    	 $scope.$on('leafletDirectiveGeoJson.click', function(event, args){
    		 console.log("click on leafletDirectiveMarker"+event);
    		 
    		 //initialize everything here ;
    		 var markerName = args.leafletEvent.target.options.name; //has to be set above
    		 var portfolioURL = args.model.properties.url;    		 
             $scope.titleSelected  =  args.model.properties.title;;
             console.log("$scope.titleSelected"+$scope.titleSelected);
             $scope.descrSelected = args.model.properties.description;;
             $scope.urlLink  = args.model.properties.url; 
             console.log("  $scope.urlLink"+  $scope.urlLink);
             $scope.media=args.model.properties.mediatype;
             
             //based on the media type change the template url;
    		 
    		 $uibModal.open({
                      
    			 		templateUrl: 'scripts/app/main/showimagetemplate.html',
                    
    			 		scope: $scope, //passed current scope to the modal
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: "knknkssdnkn",
                                    description: null,
                                    address: null,
                                    latitude: null,
                                    longitude: null,
                                    consolidatedTags: null,
                                    urlOrfileLink: null,
                                    audioFile: null,
                                    audioFileContentType: null,
                                    id: null
                                };
                            }
                        }
                    });
    		  
    		 
     
             
    	 });
 
        
        $scope.markerLat = 23.200000;
        $scope.markerLng = 79.225487;
        $scope.infoTitle = 'India';
        $scope.baseUrl;
        
        
        
        $scope.listOfPoints = 
        	[
                               
                               { title: "", description: "", Address : "", Latitude : 0,Longitude :0,UrlOrfileLink :""} 
                               
            ];
        
        $scope.initBaseURL = function ($location)
        {
        	
        	 $scope.baseUrl = $location.absUrl();
        	 console.log(" $scope.baseUrl"+ $scope.baseUrl);
        }
        
        $scope.initBaseURL($location);
        
         
       
  
        
        
        
 }  ] );
