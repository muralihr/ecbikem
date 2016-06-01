(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('HourlyRentalCharges', HourlyRentalCharges);

    HourlyRentalCharges.$inject = ['$resource'];

    function HourlyRentalCharges ($resource) {
        var resourceUrl =  'api/hourly-rental-charges/:id';

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
