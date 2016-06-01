(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('member-bill', {
            parent: 'entity',
            url: '/member-bill?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.memberBill.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member-bill/member-bills.html',
                    controller: 'MemberBillController',
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
                    $translatePartialLoader.addPart('memberBill');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('member-bill-detail', {
            parent: 'entity',
            url: '/member-bill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.memberBill.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member-bill/member-bill-detail.html',
                    controller: 'MemberBillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('memberBill');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MemberBill', function($stateParams, MemberBill) {
                    return MemberBill.get({id : $stateParams.id});
                }]
            }
        })
        .state('member-bill.new', {
            parent: 'member-bill',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-bill/member-bill-dialog.html',
                    controller: 'MemberBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateOfPayment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('member-bill', null, { reload: true });
                }, function() {
                    $state.go('member-bill');
                });
            }]
        })
        .state('member-bill.edit', {
            parent: 'member-bill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-bill/member-bill-dialog.html',
                    controller: 'MemberBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MemberBill', function(MemberBill) {
                            return MemberBill.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('member-bill', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('member-bill.delete', {
            parent: 'member-bill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-bill/member-bill-delete-dialog.html',
                    controller: 'MemberBillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MemberBill', function(MemberBill) {
                            return MemberBill.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('member-bill', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
