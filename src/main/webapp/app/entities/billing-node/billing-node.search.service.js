(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('BillingNodeSearch', BillingNodeSearch);

    BillingNodeSearch.$inject = ['$resource'];

    function BillingNodeSearch($resource) {
        var resourceUrl =  'api/_search/billing-nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
