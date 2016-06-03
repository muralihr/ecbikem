(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rental-trip', {
            parent: 'entity',
            url: '/rental-trip?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.rentalTrip.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-trip/rental-trips.html',
                    controller: 'RentalTripController',
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
                    $translatePartialLoader.addPart('rentalTrip');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rental-trip-detail', {
            parent: 'entity',
            url: '/rental-trip/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.rentalTrip.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-trip/rental-trip-detail.html',
                    controller: 'RentalTripDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rentalTrip');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RentalTrip', function($stateParams, RentalTrip) {
                    return RentalTrip.get({id : $stateParams.id});
                }]
            }
        })
          .state('rental-trip-start', {
            parent: 'entity',
            url: '/rental-trips/starttrip',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'START TRIP - ENTER MEMBER ID'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-trip/startRentalTripPage.html',
                    controller: 'RentalTripServiceController' 
                  
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rentalTrip');
                    return $translate.refresh();
                }]
                
            }
        })
        .state('rental-trip-stop', {
            parent: 'entity',
            url: '/rental-trips/stoptrip',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.rentalTrip.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-trip/stopRentalTripPage.html',
                    controller: 'RentalTripServiceController' 
                  
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rentalTrip');
                    return $translate.refresh();
                }]
                
            }
        })
        .state('rental-trip-oktrips', {
                parent: 'entity',
                url: '/rental-trips/oktrips',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Report Successful Rental Trips'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rental-trip/oktrips.html',
                        controller: 'RentalTripServiceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rentalTrip');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        .state('rental-trip.new', {
            parent: 'rental-trip',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-trip/rental-trip-dialog.html',
                    controller: 'RentalTripDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rentStartTime: null,
                                rentStopTime: null,
                                fineApplied: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rental-trip', null, { reload: true });
                }, function() {
                    $state.go('rental-trip');
                });
            }]
        })
        .state('rental-trip.edit', {
            parent: 'rental-trip',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-trip/rental-trip-dialog.html',
                    controller: 'RentalTripDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RentalTrip', function(RentalTrip) {
                            return RentalTrip.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rental-trip', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rental-trip.delete', {
            parent: 'rental-trip',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-trip/rental-trip-delete-dialog.html',
                    controller: 'RentalTripDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RentalTrip', function(RentalTrip) {
                            return RentalTrip.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rental-trip', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
