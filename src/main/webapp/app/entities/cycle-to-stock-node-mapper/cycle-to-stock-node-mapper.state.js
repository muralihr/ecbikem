(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cycle-to-stock-node-mapper', {
            parent: 'entity',
            url: '/cycle-to-stock-node-mapper?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToStockNodeMapper.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/cycle-to-stock-node-mappers.html',
                    controller: 'CycleToStockNodeMapperController',
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
                    $translatePartialLoader.addPart('cycleToStockNodeMapper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-to-stock-node-mapper-detail', {
            parent: 'entity',
            url: '/cycle-to-stock-node-mapper/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToStockNodeMapper.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/cycle-to-stock-node-mapper-detail.html',
                    controller: 'CycleToStockNodeMapperDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycleToStockNodeMapper');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CycleToStockNodeMapper', function($stateParams, CycleToStockNodeMapper) {
                    return CycleToStockNodeMapper.get({id : $stateParams.id});
                }]
            }
        })
        .state('cycle-to-stock-node-mapper-moveCycleToStockNode', {
            parent: 'entity',
            url: '/moveCycleToStockNode',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveCycleToStockNode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/moveCycleToStockNode.html',
                    controller: 'MoveCycleToStockNodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycleToStockNodeMapper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-to-stock-node-mapper.new', {
            parent: 'cycle-to-stock-node-mapper',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/cycle-to-stock-node-mapper-dialog.html',
                    controller: 'CycleToStockNodeMapperDialogController',
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
                    $state.go('cycle-to-stock-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('cycle-to-stock-node-mapper');
                });
            }]
        })
        .state('cycle-to-stock-node-mapper.edit', {
            parent: 'cycle-to-stock-node-mapper',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/cycle-to-stock-node-mapper-dialog.html',
                    controller: 'CycleToStockNodeMapperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CycleToStockNodeMapper', function(CycleToStockNodeMapper) {
                            return CycleToStockNodeMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-stock-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
       
        .state('cycle-to-stock-node-mapper.delete', {
            parent: 'cycle-to-stock-node-mapper',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-stock-node-mapper/cycle-to-stock-node-mapper-delete-dialog.html',
                    controller: 'CycleToStockNodeMapperDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CycleToStockNodeMapper', function(CycleToStockNodeMapper) {
                            return CycleToStockNodeMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-stock-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
