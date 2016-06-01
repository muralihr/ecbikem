(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('CycleToRentalNodeMapperSearch', CycleToRentalNodeMapperSearch);

    CycleToRentalNodeMapperSearch.$inject = ['$resource'];

    function CycleToRentalNodeMapperSearch($resource) {
        var resourceUrl =  'api/_search/cycle-to-rental-node-mappers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
