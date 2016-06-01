(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .factory('MemberMobileSearch', MemberMobileSearch);

    MemberMobileSearch.$inject = ['$resource'];

    function MemberMobileSearch($resource) {
        var resourceUrl =  'api/_search/member-mobiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
