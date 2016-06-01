'use strict';

describe('Controller Tests', function() {

    describe('RentalBufferNode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRentalBufferNode, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRentalBufferNode = jasmine.createSpy('MockRentalBufferNode');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RentalBufferNode': MockRentalBufferNode,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("RentalBufferNodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:rentalBufferNodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
