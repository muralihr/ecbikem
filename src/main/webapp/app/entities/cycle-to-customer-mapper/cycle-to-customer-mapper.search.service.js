(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('CycleToCustomerMapperSearch', CycleToCustomerMapperSearch);

    CycleToCustomerMapperSearch.$inject = ['$resource'];

    function CycleToCustomerMapperSearch($resource) {
        var resourceUrl =  'api/_search/cycle-to-customer-mappers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
