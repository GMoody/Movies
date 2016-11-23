(function() {
    'use strict';

    angular
        .module('moviesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('writer', {
            parent: 'entity',
            url: '/writer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moviesApp.writer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/writer/writers.html',
                    controller: 'WriterController',
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
                    $translatePartialLoader.addPart('writer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('writer-detail', {
            parent: 'entity',
            url: '/writer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moviesApp.writer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/writer/writer-detail.html',
                    controller: 'WriterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('writer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Writer', function($stateParams, Writer) {
                    return Writer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'writer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('writer-detail.edit', {
            parent: 'writer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/writer/writer-dialog.html',
                    controller: 'WriterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Writer', function(Writer) {
                            return Writer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('writer.new', {
            parent: 'writer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/writer/writer-dialog.html',
                    controller: 'WriterDialogController',
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
                    $state.go('writer', null, { reload: 'writer' });
                }, function() {
                    $state.go('writer');
                });
            }]
        })
        .state('writer.edit', {
            parent: 'writer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/writer/writer-dialog.html',
                    controller: 'WriterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Writer', function(Writer) {
                            return Writer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('writer', null, { reload: 'writer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('writer.delete', {
            parent: 'writer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/writer/writer-delete-dialog.html',
                    controller: 'WriterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Writer', function(Writer) {
                            return Writer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('writer', null, { reload: 'writer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
