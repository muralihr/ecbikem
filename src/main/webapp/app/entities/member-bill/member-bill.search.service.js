(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('MemberBillSearch', MemberBillSearch);

    MemberBillSearch.$inject = ['$resource'];

    function MemberBillSearch($resource) {
        var resourceUrl =  'api/_search/member-bills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
