'use strict';

describe('Controller Tests', function() {

    describe('HourlyRentalCharges Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockHourlyRentalCharges;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockHourlyRentalCharges = jasmine.createSpy('MockHourlyRentalCharges');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'HourlyRentalCharges': MockHourlyRentalCharges
            };
            createController = function() {
                $injector.get('$controller')("HourlyRentalChargesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:hourlyRentalChargesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
