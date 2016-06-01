(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trip-rent-units-to-charge-map', {
            parent: 'entity',
            url: '/trip-rent-units-to-charge-map?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.tripRentUnitsToChargeMap.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trip-rent-units-to-charge-map/trip-rent-units-to-charge-maps.html',
                    controller: 'TripRentUnitsToChargeMapController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tripRentUnitsToChargeMap');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trip-rent-units-to-charge-map-detail', {
            parent: 'entity',
            url: '/trip-rent-units-to-charge-map/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.tripRentUnitsToChargeMap.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trip-rent-units-to-charge-map/trip-rent-units-to-charge-map-detail.html',
                    controller: 'TripRentUnitsToChargeMapDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tripRentUnitsToChargeMap');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TripRentUnitsToChargeMap', function($stateParams, TripRentUnitsToChargeMap) {
                    return TripRentUnitsToChargeMap.get({id : $stateParams.id});
                }]
            }
        })
        .state('trip-rent-units-to-charge-map.new', {
            parent: 'trip-rent-units-to-charge-map',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip-rent-units-to-charge-map/trip-rent-units-to-charge-map-dialog.html',
                    controller: 'TripRentUnitsToChargeMapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeOfPassOrFare: null,
                                prePaidUnits: null,
                                chargesForPaidUnits: null,
                                expirationPeriodInMonths: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trip-rent-units-to-charge-map', null, { reload: true });
                }, function() {
                    $state.go('trip-rent-units-to-charge-map');
                });
            }]
        })
        .state('trip-rent-units-to-charge-map.edit', {
            parent: 'trip-rent-units-to-charge-map',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip-rent-units-to-charge-map/trip-rent-units-to-charge-map-dialog.html',
                    controller: 'TripRentUnitsToChargeMapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TripRentUnitsToChargeMap', function(TripRentUnitsToChargeMap) {
                            return TripRentUnitsToChargeMap.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('trip-rent-units-to-charge-map', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trip-rent-units-to-charge-map.delete', {
            parent: 'trip-rent-units-to-charge-map',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip-rent-units-to-charge-map/trip-rent-units-to-charge-map-delete-dialog.html',
                    controller: 'TripRentUnitsToChargeMapDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TripRentUnitsToChargeMap', function(TripRentUnitsToChargeMap) {
                            return TripRentUnitsToChargeMap.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('trip-rent-units-to-charge-map', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
