(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('BillingNode', BillingNode);

    BillingNode.$inject = ['$resource'];

    function BillingNode ($resource) {
        var resourceUrl =  'api/billing-nodes/:id';

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
