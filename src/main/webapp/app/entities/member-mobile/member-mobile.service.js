(function() {
    'use strict';
    angular
        .module('ecbikeApp')
        .factory('MemberMobile', MemberMobile);

    MemberMobile.$inject = ['$resource', 'DateUtils'];

    function MemberMobile ($resource, DateUtils) {
        var resourceUrl =  'api/member-mobiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateOfBirth = DateUtils.convertLocalDateFromServer(data.dateOfBirth);
                    data.dateOfExpiration = DateUtils.convertLocalDateFromServer(data.dateOfExpiration);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateOfBirth = DateUtils.convertLocalDateToServer(data.dateOfBirth);
                    data.dateOfExpiration = DateUtils.convertLocalDateToServer(data.dateOfExpiration);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateOfBirth = DateUtils.convertLocalDateToServer(data.dateOfBirth);
                    data.dateOfExpiration = DateUtils.convertLocalDateToServer(data.dateOfExpiration);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
