(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('RentalBufferNode', RentalBufferNode);

    RentalBufferNode.$inject = ['$resource'];

    function RentalBufferNode ($resource) {
        var resourceUrl =  'api/rental-buffer-nodes/:id';

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
