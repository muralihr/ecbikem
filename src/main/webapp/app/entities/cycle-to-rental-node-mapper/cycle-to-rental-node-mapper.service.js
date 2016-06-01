(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('CycleToRentalNodeMapper', CycleToRentalNodeMapper);

    CycleToRentalNodeMapper.$inject = ['$resource'];

    function CycleToRentalNodeMapper ($resource) {
        var resourceUrl =  'api/cycle-to-rental-node-mappers/:id';

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
