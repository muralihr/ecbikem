'use strict';

describe('Controller Tests', function() {

    describe('CycleToStockNodeMapper Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCycleToStockNodeMapper, MockStockBufferNode, MockBicycle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCycleToStockNodeMapper = jasmine.createSpy('MockCycleToStockNodeMapper');
            MockStockBufferNode = jasmine.createSpy('MockStockBufferNode');
            MockBicycle = jasmine.createSpy('MockBicycle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CycleToStockNodeMapper': MockCycleToStockNodeMapper,
                'StockBufferNode': MockStockBufferNode,
                'Bicycle': MockBicycle
            };
            createController = function() {
                $injector.get('$controller')("CycleToStockNodeMapperDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:cycleToStockNodeMapperUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
