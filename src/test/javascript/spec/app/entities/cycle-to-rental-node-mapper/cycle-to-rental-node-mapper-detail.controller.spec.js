'use strict';

describe('Controller Tests', function() {

    describe('CycleToRentalNodeMapper Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCycleToRentalNodeMapper, MockRentalBufferNode, MockBicycle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCycleToRentalNodeMapper = jasmine.createSpy('MockCycleToRentalNodeMapper');
            MockRentalBufferNode = jasmine.createSpy('MockRentalBufferNode');
            MockBicycle = jasmine.createSpy('MockBicycle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CycleToRentalNodeMapper': MockCycleToRentalNodeMapper,
                'RentalBufferNode': MockRentalBufferNode,
                'Bicycle': MockBicycle
            };
            createController = function() {
                $injector.get('$controller')("CycleToRentalNodeMapperDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:cycleToRentalNodeMapperUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
