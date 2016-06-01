(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('TripRentUnitsToChargeMapSearch', TripRentUnitsToChargeMapSearch);

    TripRentUnitsToChargeMapSearch.$inject = ['$resource'];

    function TripRentUnitsToChargeMapSearch($resource) {
        var resourceUrl =  'api/_search/trip-rent-units-to-charge-maps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
