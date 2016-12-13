(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('FavouritesController', FavouritesController);

    FavouritesController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'pagingParams', 'Favourites', 'User', 'paginationConstants'];

    function FavouritesController($scope, $state, ParseLinks, AlertService, pagingParams, Favourites, User, paginationConstants) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.favourites = null;
        vm.isAuthenticated = null;
        vm.removeFollower = removeFollower;

        loadAll();

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function loadAll() {
            Favourites.getCurrentUserFavourites({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.favourites = data;
                // vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function removeFollower(movie) {
            Favourites.removeCurrentFollower({movieID: movie.id});
            $state.reload();
        }
    }
})();
