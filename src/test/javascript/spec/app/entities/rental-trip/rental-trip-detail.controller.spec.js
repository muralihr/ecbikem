'use strict';

describe('Controller Tests', function() {

    describe('RentalTrip Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRentalTrip, MockBicycle, MockMemberMobile, MockRentalBufferNode, MockHourlyRentalCharges;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRentalTrip = jasmine.createSpy('MockRentalTrip');
            MockBicycle = jasmine.createSpy('MockBicycle');
            MockMemberMobile = jasmine.createSpy('MockMemberMobile');
            MockRentalBufferNode = jasmine.createSpy('MockRentalBufferNode');
            MockHourlyRentalCharges = jasmine.createSpy('MockHourlyRentalCharges');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RentalTrip': MockRentalTrip,
                'Bicycle': MockBicycle,
                'MemberMobile': MockMemberMobile,
                'RentalBufferNode': MockRentalBufferNode,
                'HourlyRentalCharges': MockHourlyRentalCharges
            };
            createController = function() {
                $injector.get('$controller')("RentalTripDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:rentalTripUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
