'use strict';

describe('Controller Tests', function() {

    describe('TripRentUnitsToChargeMap Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTripRentUnitsToChargeMap;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTripRentUnitsToChargeMap = jasmine.createSpy('MockTripRentUnitsToChargeMap');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TripRentUnitsToChargeMap': MockTripRentUnitsToChargeMap
            };
            createController = function() {
                $injector.get('$controller')("TripRentUnitsToChargeMapDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:tripRentUnitsToChargeMapUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
