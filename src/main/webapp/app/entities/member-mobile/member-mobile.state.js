(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('member-mobile', {
            parent: 'entity',
            url: '/member-mobile?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.memberMobile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member-mobile/member-mobiles.html',
                    controller: 'MemberMobileController',
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
                    $translatePartialLoader.addPart('memberMobile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('member-mobile-detail', {
            parent: 'entity',
            url: '/member-mobile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecbikeApp.memberMobile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member-mobile/member-mobile-detail.html',
                    controller: 'MemberMobileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('memberMobile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MemberMobile', function($stateParams, MemberMobile) {
                    return MemberMobile.get({id : $stateParams.id});
                }]
            }
        })
        .state('member-mobile.new', {
            parent: 'member-mobile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-mobile/originalmember-mobile-dialog.html',
                    controller: 'MemberMobileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                dateOfBirth: null,
                                emailId: null,
                                mobileNo: null,
                                address1: null,
                                address2: null,
                                city: null,
                                state: null,
                                zipcode: null,
                                country: null,
                                photo: null,
                                photoContentType: null,
                                photoIDProof: null,
                                photoIDProofContentType: null,
                                myCurrentRentUnits: null,
                                myChargedRentUnits: null,
                                behaviorStatus: null,
                                myCurrentFineCharges: null,
                                dateOfExpiration: null,
                                userName: null,
                                passWord: null,
                                activated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('member-mobile', null, { reload: true });
                }, function() {
                    $state.go('member-mobile');
                });
            }]
        })
        .state('member-mobile.edit', {
            parent: 'member-mobile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-mobile/originalmember-mobile-dialog.html',
                    controller: 'MemberMobileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MemberMobile', function(MemberMobile) {
                            return MemberMobile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('member-mobile', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('member-mobile.delete', {
            parent: 'member-mobile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member-mobile/member-mobile-delete-dialog.html',
                    controller: 'MemberMobileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MemberMobile', function(MemberMobile) {
                            return MemberMobile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('member-mobile', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
