(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-buffer-node', {
            parent: 'entity',
            url: '/stock-buffer-node?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.stockBufferNode.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-buffer-node/stock-buffer-nodes.html',
                    controller: 'StockBufferNodeController',
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
                    $translatePartialLoader.addPart('stockBufferNode');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-buffer-node-detail', {
            parent: 'entity',
            url: '/stock-buffer-node/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.stockBufferNode.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-buffer-node/stock-buffer-node-detail.html',
                    controller: 'StockBufferNodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockBufferNode');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockBufferNode', function($stateParams, StockBufferNode) {
                    return StockBufferNode.get({id : $stateParams.id});
                }]
            }
        })
        .state('stock-buffer-node.new', {
            parent: 'stock-buffer-node',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-buffer-node/stock-buffer-node-dialog.html',
                    controller: 'StockBufferNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                godownName: null,
                                storageCapacity: null,
                                address1: null,
                                address2: null,
                                city: null,
                                state: null,
                                zipcode: null,
                                longitudePos: null,
                                latitudePos: null,
                                colorCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('stock-buffer-node');
                });
            }]
        })
        .state('stock-buffer-node.edit', {
            parent: 'stock-buffer-node',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-buffer-node/stock-buffer-node-dialog.html',
                    controller: 'StockBufferNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockBufferNode', function(StockBufferNode) {
                            return StockBufferNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-buffer-node.delete', {
            parent: 'stock-buffer-node',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-buffer-node/stock-buffer-node-delete-dialog.html',
                    controller: 'StockBufferNodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockBufferNode', function(StockBufferNode) {
                            return StockBufferNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
