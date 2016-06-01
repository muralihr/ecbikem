'use strict';

describe('Controller Tests', function() {

    describe('TypeOfIDCard Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTypeOfIDCard;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTypeOfIDCard = jasmine.createSpy('MockTypeOfIDCard');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TypeOfIDCard': MockTypeOfIDCard
            };
            createController = function() {
                $injector.get('$controller')("TypeOfIDCardDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:typeOfIDCardUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
