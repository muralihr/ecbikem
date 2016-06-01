(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('StockBufferNode', StockBufferNode);

    StockBufferNode.$inject = ['$resource'];

    function StockBufferNode ($resource) {
        var resourceUrl =  'api/stock-buffer-nodes/:id';

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
