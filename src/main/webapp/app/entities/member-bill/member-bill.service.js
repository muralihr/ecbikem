(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('MemberBill', MemberBill);

    MemberBill.$inject = ['$resource', 'DateUtils'];

    function MemberBill ($resource, DateUtils) {
        var resourceUrl =  'api/member-bills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateOfPayment = DateUtils.convertDateTimeFromServer(data.dateOfPayment);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
