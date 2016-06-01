(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('TripRentUnitsToChargeMap', TripRentUnitsToChargeMap);

    TripRentUnitsToChargeMap.$inject = ['$resource'];

    function TripRentUnitsToChargeMap ($resource) {
        var resourceUrl =  'api/trip-rent-units-to-charge-maps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
