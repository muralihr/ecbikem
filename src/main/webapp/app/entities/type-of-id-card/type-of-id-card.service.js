(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('TypeOfIDCard', TypeOfIDCard);

    TypeOfIDCard.$inject = ['$resource'];

    function TypeOfIDCard ($resource) {
        var resourceUrl =  'api/type-of-id-cards/:id';

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
