(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bicycle', {
            parent: 'entity',
            url: '/bicycle?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.bicycle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bicycle/bicycles.html',
                    controller: 'BicycleController',
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
                    $translatePartialLoader.addPart('bicycle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bicycle-detail', {
            parent: 'entity',
            url: '/bicycle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.bicycle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bicycle/bicycle-detail.html',
                    controller: 'BicycleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bicycle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bicycle', function($stateParams, Bicycle) {
                    return Bicycle.get({id : $stateParams.id});
                }]
            }
        })
        .state('bicycle.new', {
            parent: 'bicycle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bicycle/bicycle-dialog.html',
                    controller: 'BicycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tagId: null,
                                cycleManufacturer: null,
                                dateOfPurchase: null,
                                costOfCycle: null,
                                insuranceNo: null,
                                moveStatus: null,
                                insuranceStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bicycle', null, { reload: true });
                }, function() {
                    $state.go('bicycle');
                });
            }]
        })
        .state('bicycle.edit', {
            parent: 'bicycle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bicycle/bicycle-dialog.html',
                    controller: 'BicycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bicycle', function(Bicycle) {
                            return Bicycle.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bicycle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bicycle.delete', {
            parent: 'bicycle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bicycle/bicycle-delete-dialog.html',
                    controller: 'BicycleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bicycle', function(Bicycle) {
                            return Bicycle.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bicycle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
