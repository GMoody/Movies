(function() {
    'use strict';

    angular
        .module('moviesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('director', {
            parent: 'entity',
            url: '/director?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moviesApp.director.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director/directors.html',
                    controller: 'DirectorController',
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
                    $translatePartialLoader.addPart('director');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('director-detail', {
            parent: 'entity',
            url: '/director/{id}',
            data: {
                authorities: [],
                pageTitle: 'moviesApp.director.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/director/director-detail.html',
                    controller: 'DirectorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('director');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Director', function($stateParams, Director) {
                    return Director.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'director',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('director-detail.edit', {
            parent: 'director-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director/director-dialog.html',
                    controller: 'DirectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Director', function(Director) {
                            return Director.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director.new', {
            parent: 'director',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director/director-dialog.html',
                    controller: 'DirectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                avatarURL: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('director', null, { reload: 'director' });
                }, function() {
                    $state.go('director');
                });
            }]
        })
        .state('director.edit', {
            parent: 'director',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director/director-dialog.html',
                    controller: 'DirectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Director', function(Director) {
                            return Director.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director', null, { reload: 'director' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('director.delete', {
            parent: 'director',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/director/director-delete-dialog.html',
                    controller: 'DirectorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Director', function(Director) {
                            return Director.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('director', null, { reload: 'director' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
