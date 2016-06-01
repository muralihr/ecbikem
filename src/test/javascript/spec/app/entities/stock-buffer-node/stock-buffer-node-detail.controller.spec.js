'use strict';

describe('Controller Tests', function() {

    describe('StockBufferNode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStockBufferNode, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStockBufferNode = jasmine.createSpy('MockStockBufferNode');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StockBufferNode': MockStockBufferNode,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("StockBufferNodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:stockBufferNodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
