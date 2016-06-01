/**
 * 
 */

angular.module('ecbikeApp' )
.service('sharedGeoProperties', function () {
    var longitude = 0;
    var latitude = 0 ;
    
    var geoJSON = null;

    return {
        getLongitude: function () {
            return longitude;
        },
        setLongitude: function(value) {
        	console.log("setLongitude"+value);
        	longitude = value;
        },
        
        getLatitude: function () {
            return latitude;
        },
        setLatitude: function(value) {
        	console.log("setLatitude"+value);
        	latitude = value;
        },        
        getGeoJSON: function () {
            return geoJSON;
        },
        setGeoJSON: function(value) {
        	console.log("setGeoJSON"+value);
        	geoJSON = value;
        }
    };
});


