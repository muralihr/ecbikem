(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hourly-rental-charges', {
            parent: 'entity',
            url: '/hourly-rental-charges?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.hourlyRentalCharges.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hourly-rental-charges/hourly-rental-charges.html',
                    controller: 'HourlyRentalChargesController',
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
                    $translatePartialLoader.addPart('hourlyRentalCharges');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hourly-rental-charges-detail', {
            parent: 'entity',
            url: '/hourly-rental-charges/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.hourlyRentalCharges.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hourly-rental-charges/hourly-rental-charges-detail.html',
                    controller: 'HourlyRentalChargesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hourlyRentalCharges');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HourlyRentalCharges', function($stateParams, HourlyRentalCharges) {
                    return HourlyRentalCharges.get({id : $stateParams.id});
                }]
            }
        })
        .state('hourly-rental-charges.new', {
            parent: 'hourly-rental-charges',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hourly-rental-charges/hourly-rental-charges-dialog.html',
                    controller: 'HourlyRentalChargesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                deductableUnits: null,
                                rentedHours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hourly-rental-charges', null, { reload: true });
                }, function() {
                    $state.go('hourly-rental-charges');
                });
            }]
        })
        .state('hourly-rental-charges.edit', {
            parent: 'hourly-rental-charges',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hourly-rental-charges/hourly-rental-charges-dialog.html',
                    controller: 'HourlyRentalChargesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HourlyRentalCharges', function(HourlyRentalCharges) {
                            return HourlyRentalCharges.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('hourly-rental-charges', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hourly-rental-charges.delete', {
            parent: 'hourly-rental-charges',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hourly-rental-charges/hourly-rental-charges-delete-dialog.html',
                    controller: 'HourlyRentalChargesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HourlyRentalCharges', function(HourlyRentalCharges) {
                            return HourlyRentalCharges.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('hourly-rental-charges', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
