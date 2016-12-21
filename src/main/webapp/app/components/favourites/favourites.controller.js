(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('FavouritesController', FavouritesController);

    FavouritesController.$inject = ['$state', 'ParseLinks', 'AlertService', 'pagingParams', 'Favourites', 'paginationConstants'];

    function FavouritesController($state, ParseLinks, AlertService, pagingParams, Favourites, paginationConstants) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.favourites = null;
        vm.isAuthenticated = null;

        vm.removeFollower = removeFollower;

        loadAll();

        function loadAll() {
            Favourites.getCurrentUserFavourites({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.favourites = data;
                vm.links = ParseLinks.parse(headers('link'));
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
        }

        function removeFollower(movie) {
            Favourites.removeCurrentFollower({movieID: movie.id});
            $state.reload();
        }
    }
})();
