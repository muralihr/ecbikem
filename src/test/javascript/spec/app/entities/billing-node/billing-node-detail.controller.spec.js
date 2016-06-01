'use strict';

describe('Controller Tests', function() {

    describe('BillingNode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBillingNode, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBillingNode = jasmine.createSpy('MockBillingNode');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BillingNode': MockBillingNode,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("BillingNodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:billingNodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
