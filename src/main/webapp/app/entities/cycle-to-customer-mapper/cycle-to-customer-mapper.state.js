(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cycle-to-customer-mapper', {
            parent: 'entity',
            url: '/cycle-to-customer-mapper?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToCustomerMapper.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-customer-mapper/cycle-to-customer-mappers.html',
                    controller: 'CycleToCustomerMapperController',
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
                    $translatePartialLoader.addPart('cycleToCustomerMapper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-to-customer-mapper-detail', {
            parent: 'entity',
            url: '/cycle-to-customer-mapper/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToCustomerMapper.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-customer-mapper/cycle-to-customer-mapper-detail.html',
                    controller: 'CycleToCustomerMapperDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycleToCustomerMapper');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CycleToCustomerMapper', function($stateParams, CycleToCustomerMapper) {
                    return CycleToCustomerMapper.get({id : $stateParams.id});
                }]
            }
        })
        .state('cycle-to-customer-mapper.new', {
            parent: 'cycle-to-customer-mapper',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-customer-mapper/cycle-to-customer-mapper-dialog.html',
                    controller: 'CycleToCustomerMapperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cycle-to-customer-mapper', null, { reload: true });
                }, function() {
                    $state.go('cycle-to-customer-mapper');
                });
            }]
        })
        .state('cycle-to-customer-mapper.edit', {
            parent: 'cycle-to-customer-mapper',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-customer-mapper/cycle-to-customer-mapper-dialog.html',
                    controller: 'CycleToCustomerMapperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CycleToCustomerMapper', function(CycleToCustomerMapper) {
                            return CycleToCustomerMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-customer-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cycle-to-customer-mapper.delete', {
            parent: 'cycle-to-customer-mapper',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-customer-mapper/cycle-to-customer-mapper-delete-dialog.html',
                    controller: 'CycleToCustomerMapperDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CycleToCustomerMapper', function(CycleToCustomerMapper) {
                            return CycleToCustomerMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-customer-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
