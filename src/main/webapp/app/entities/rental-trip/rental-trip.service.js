(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('RentalTrip', RentalTrip);

    RentalTrip.$inject = ['$resource', 'DateUtils'];

    function RentalTrip ($resource, DateUtils) {
        var resourceUrl =  'api/rental-trips/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.rentStartTime = DateUtils.convertDateTimeFromServer(data.rentStartTime);
                    data.rentStopTime = DateUtils.convertDateTimeFromServer(data.rentStopTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
