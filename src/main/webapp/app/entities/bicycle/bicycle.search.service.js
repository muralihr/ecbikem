(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('BicycleSearch', BicycleSearch);

    BicycleSearch.$inject = ['$resource'];

    function BicycleSearch($resource) {
        var resourceUrl =  'api/_search/bicycles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
