(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('Bicycle', Bicycle);

    Bicycle.$inject = ['$resource', 'DateUtils'];

    function Bicycle ($resource, DateUtils) {
        var resourceUrl =  'api/bicycles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateOfPurchase = DateUtils.convertLocalDateFromServer(data.dateOfPurchase);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateOfPurchase = DateUtils.convertLocalDateToServer(data.dateOfPurchase);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateOfPurchase = DateUtils.convertLocalDateToServer(data.dateOfPurchase);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
