(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cycle-to-rental-node-mapper', {
            parent: 'entity',
            url: '/cycle-to-rental-node-mapper?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToRentalNodeMapper.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/cycle-to-rental-node-mappers.html',
                    controller: 'CycleToRentalNodeMapperController',
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
                    $translatePartialLoader.addPart('cycleToRentalNodeMapper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-to-rental-node-mapper-detail', {
            parent: 'entity',
            url: '/cycle-to-rental-node-mapper/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.cycleToRentalNodeMapper.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/cycle-to-rental-node-mapper-detail.html',
                    controller: 'CycleToRentalNodeMapperDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycleToRentalNodeMapper');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CycleToRentalNodeMapper', function($stateParams, CycleToRentalNodeMapper) {
                    return CycleToRentalNodeMapper.get({id : $stateParams.id});
                }]
            }
        })
        .state('cycle-to-rental-node-mapper.new', {
            parent: 'cycle-to-rental-node-mapper',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/cycle-to-rental-node-mapper-dialog.html',
                    controller: 'CycleToRentalNodeMapperDialogController',
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
                    $state.go('cycle-to-rental-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('cycle-to-rental-node-mapper');
                });
            }]
        })
        .state('cycle-to-rental-node-mapper.edit', {
            parent: 'cycle-to-rental-node-mapper',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/cycle-to-rental-node-mapper-dialog.html',
                    controller: 'CycleToRentalNodeMapperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CycleToRentalNodeMapper', function(CycleToRentalNodeMapper) {
                            return CycleToRentalNodeMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-rental-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
          .state('cycle-to-rental-node-mapper.moveCycleToRentalNode', {
            parent: 'entity',
            url: '/moveCycleToRentalNode',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveCycleToRentalNode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/moveCycleToRentalNode.html',
                    controller: 'MoveCycleToRentalNodeController'
                    
                }
            },
            resolve: {
                
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cycleToRentalNodeMapper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cycle-to-rental-node-mapper.delete', {
            parent: 'cycle-to-rental-node-mapper',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cycle-to-rental-node-mapper/cycle-to-rental-node-mapper-delete-dialog.html',
                    controller: 'CycleToRentalNodeMapperDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CycleToRentalNodeMapper', function(CycleToRentalNodeMapper) {
                            return CycleToRentalNodeMapper.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cycle-to-rental-node-mapper', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
