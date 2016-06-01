'use strict';

describe('Controller Tests', function() {

    describe('MemberBill Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMemberBill, MockBillingNode, MockMemberMobile, MockTripRentUnitsToChargeMap;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMemberBill = jasmine.createSpy('MockMemberBill');
            MockBillingNode = jasmine.createSpy('MockBillingNode');
            MockMemberMobile = jasmine.createSpy('MockMemberMobile');
            MockTripRentUnitsToChargeMap = jasmine.createSpy('MockTripRentUnitsToChargeMap');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MemberBill': MockMemberBill,
                'BillingNode': MockBillingNode,
                'MemberMobile': MockMemberMobile,
                'TripRentUnitsToChargeMap': MockTripRentUnitsToChargeMap
            };
            createController = function() {
                $injector.get('$controller')("MemberBillDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ecbikeApp:memberBillUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
