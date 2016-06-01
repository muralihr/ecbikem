(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('CycleToStockNodeMapperSearch', CycleToStockNodeMapperSearch);

    CycleToStockNodeMapperSearch.$inject = ['$resource'];

    function CycleToStockNodeMapperSearch($resource) {
        var resourceUrl =  'api/_search/cycle-to-stock-node-mappers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
