(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('CycleToCustomerMapper', CycleToCustomerMapper);

    CycleToCustomerMapper.$inject = ['$resource'];

    function CycleToCustomerMapper ($resource) {
        var resourceUrl =  'api/cycle-to-customer-mappers/:id';

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
