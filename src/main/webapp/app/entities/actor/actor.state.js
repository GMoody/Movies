(function() {
    'use strict';

    angular
        .module('moviesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('actor', {
            parent: 'entity',
            url: '/actor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moviesApp.actor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/actor/actors.html',
                    controller: 'ActorController',
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
                    $translatePartialLoader.addPart('actor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('actor-detail', {
            parent: 'entity',
            url: '/actor/{id}',
            data: {
                authorities: [],
                pageTitle: 'moviesApp.actor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/actor/actor-detail.html',
                    controller: 'ActorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('actor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Actor', function($stateParams, Actor) {
                    return Actor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'actor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('actor-detail.edit', {
            parent: 'actor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actor/actor-dialog.html',
                    controller: 'ActorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Actor', function(Actor) {
                            return Actor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('actor.new', {
            parent: 'actor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actor/actor-dialog.html',
                    controller: 'ActorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('actor', null, { reload: 'actor' });
                }, function() {
                    $state.go('actor');
                });
            }]
        })
        .state('actor.edit', {
            parent: 'actor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actor/actor-dialog.html',
                    controller: 'ActorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Actor', function(Actor) {
                            return Actor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('actor', null, { reload: 'actor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('actor.delete', {
            parent: 'actor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/actor/actor-delete-dialog.html',
                    controller: 'ActorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Actor', function(Actor) {
                            return Actor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('actor', null, { reload: 'actor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
