(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('TypeOfIDCardSearch', TypeOfIDCardSearch);

    TypeOfIDCardSearch.$inject = ['$resource'];

    function TypeOfIDCardSearch($resource) {
        var resourceUrl =  'api/_search/type-of-id-cards/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
