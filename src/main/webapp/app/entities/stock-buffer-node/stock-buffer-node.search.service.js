(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('StockBufferNodeSearch', StockBufferNodeSearch);

    StockBufferNodeSearch.$inject = ['$resource'];

    function StockBufferNodeSearch($resource) {
        var resourceUrl =  'api/_search/stock-buffer-nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
