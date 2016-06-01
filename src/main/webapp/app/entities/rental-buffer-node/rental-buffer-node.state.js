(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rental-buffer-node', {
            parent: 'entity',
            url: '/rental-buffer-node?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.rentalBufferNode.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-buffer-node/rental-buffer-nodes.html',
                    controller: 'RentalBufferNodeController',
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
                    $translatePartialLoader.addPart('rentalBufferNode');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rental-buffer-node-detail', {
            parent: 'entity',
            url: '/rental-buffer-node/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.rentalBufferNode.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rental-buffer-node/rental-buffer-node-detail.html',
                    controller: 'RentalBufferNodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rentalBufferNode');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RentalBufferNode', function($stateParams, RentalBufferNode) {
                    return RentalBufferNode.get({id : $stateParams.id});
                }]
            }
        })
        .state('rental-buffer-node.new', {
            parent: 'rental-buffer-node',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-buffer-node/rental-buffer-node-dialog.html',
                    controller: 'RentalBufferNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                stationName: null,
                                address1: null,
                                address2: null,
                                city: null,
                                state: null,
                                zipcode: null,
                                longitudePos: null,
                                latitudePos: null,
                                colorCode: null,
                                photoOfLocation: null,
                                photoOfLocationContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rental-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('rental-buffer-node');
                });
            }]
        })
        .state('rental-buffer-node.edit', {
            parent: 'rental-buffer-node',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-buffer-node/rental-buffer-node-dialog.html',
                    controller: 'RentalBufferNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RentalBufferNode', function(RentalBufferNode) {
                            return RentalBufferNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rental-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rental-buffer-node.delete', {
            parent: 'rental-buffer-node',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rental-buffer-node/rental-buffer-node-delete-dialog.html',
                    controller: 'RentalBufferNodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RentalBufferNode', function(RentalBufferNode) {
                            return RentalBufferNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('rental-buffer-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
