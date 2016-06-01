(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('RentalTripSearch', RentalTripSearch);

    RentalTripSearch.$inject = ['$resource'];

    function RentalTripSearch($resource) {
        var resourceUrl =  'api/_search/rental-trips/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
