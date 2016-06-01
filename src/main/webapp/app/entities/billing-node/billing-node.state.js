(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('billing-node', {
            parent: 'entity',
            url: '/billing-node?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.billingNode.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-node/billing-nodes.html',
                    controller: 'BillingNodeController',
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
                    $translatePartialLoader.addPart('billingNode');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('billing-node-detail', {
            parent: 'entity',
            url: '/billing-node/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.billingNode.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-node/billing-node-detail.html',
                    controller: 'BillingNodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billingNode');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BillingNode', function($stateParams, BillingNode) {
                    return BillingNode.get({id : $stateParams.id});
                }]
            }
        })
        .state('billing-node.new', {
            parent: 'billing-node',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-node/billing-node-dialog.html',
                    controller: 'BillingNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                billCenterName: null,
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
                    $state.go('billing-node', null, { reload: true });
                }, function() {
                    $state.go('billing-node');
                });
            }]
        })
        .state('billing-node.edit', {
            parent: 'billing-node',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-node/billing-node-dialog.html',
                    controller: 'BillingNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillingNode', function(BillingNode) {
                            return BillingNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billing-node.delete', {
            parent: 'billing-node',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-node/billing-node-delete-dialog.html',
                    controller: 'BillingNodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BillingNode', function(BillingNode) {
                            return BillingNode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-node', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
