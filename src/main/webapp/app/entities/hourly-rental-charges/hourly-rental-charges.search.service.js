(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('HourlyRentalChargesSearch', HourlyRentalChargesSearch);

    HourlyRentalChargesSearch.$inject = ['$resource'];

    function HourlyRentalChargesSearch($resource) {
        var resourceUrl =  'api/_search/hourly-rental-charges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
