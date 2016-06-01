(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('RentalBufferNodeSearch', RentalBufferNodeSearch);

    RentalBufferNodeSearch.$inject = ['$resource'];

    function RentalBufferNodeSearch($resource) {
        var resourceUrl =  'api/_search/rental-buffer-nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
