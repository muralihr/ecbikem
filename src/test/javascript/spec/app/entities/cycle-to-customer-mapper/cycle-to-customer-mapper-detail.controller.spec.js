'use strict';

describe('Controller Tests', function() {

    describe('CycleToCustomerMapper Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCycleToCustomerMapper, MockMemberMobile, MockBicycle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCycleToCustomerMapper = jasmine.createSpy('MockCycleToCustomerMapper');
            MockMemberMobile = jasmine.createSpy('MockMemberMobile');
            MockBicycle = jasmine.createSpy('MockBicycle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CycleToCustomerMapper': MockCycleToCustomerMapper,
                'MemberMobile': MockMemberMobile,
                'Bicycle': MockBicycle
            };
            createController = function() {
                $injector.get('$controller')("CycleToCustomerMapperDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:cycleToCustomerMapperUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
