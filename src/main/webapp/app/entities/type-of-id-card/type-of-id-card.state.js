(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-of-id-card', {
            parent: 'entity',
            url: '/type-of-id-card?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.typeOfIDCard.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-of-id-card/type-of-id-cards.html',
                    controller: 'TypeOfIDCardController',
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
                    $translatePartialLoader.addPart('typeOfIDCard');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-of-id-card-detail', {
            parent: 'entity',
            url: '/type-of-id-card/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.typeOfIDCard.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-of-id-card/type-of-id-card-detail.html',
                    controller: 'TypeOfIDCardDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeOfIDCard');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeOfIDCard', function($stateParams, TypeOfIDCard) {
                    return TypeOfIDCard.get({id : $stateParams.id});
                }]
            }
        })
        .state('type-of-id-card.new', {
            parent: 'type-of-id-card',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-of-id-card/type-of-id-card-dialog.html',
                    controller: 'TypeOfIDCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                authorizedIDName: null,
                                authorizationAgency: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-of-id-card', null, { reload: true });
                }, function() {
                    $state.go('type-of-id-card');
                });
            }]
        })
        .state('type-of-id-card.edit', {
            parent: 'type-of-id-card',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-of-id-card/type-of-id-card-dialog.html',
                    controller: 'TypeOfIDCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeOfIDCard', function(TypeOfIDCard) {
                            return TypeOfIDCard.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-of-id-card', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-of-id-card.delete', {
            parent: 'type-of-id-card',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-of-id-card/type-of-id-card-delete-dialog.html',
                    controller: 'TypeOfIDCardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeOfIDCard', function(TypeOfIDCard) {
                            return TypeOfIDCard.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-of-id-card', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
