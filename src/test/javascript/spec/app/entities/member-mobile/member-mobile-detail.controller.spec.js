'use strict';

describe('Controller Tests', function() {

    describe('MemberMobile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMemberMobile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMemberMobile = jasmine.createSpy('MockMemberMobile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MemberMobile': MockMemberMobile
            };
            createController = function() {
                $injector.get('$controller')("MemberMobileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:memberMobileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
