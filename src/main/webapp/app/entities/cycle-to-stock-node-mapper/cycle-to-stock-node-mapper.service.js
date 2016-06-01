(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('CycleToStockNodeMapper', CycleToStockNodeMapper);

    CycleToStockNodeMapper.$inject = ['$resource'];

    function CycleToStockNodeMapper ($resource) {
        var resourceUrl =  'api/cycle-to-stock-node-mappers/:id';

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
