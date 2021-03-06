(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('FavouritesController', FavouritesController);

    FavouritesController.$inject = ['$rootScope', '$state', 'ParseLinks', 'AlertService', 'pagingParams', 'Favourites', 'paginationConstants'];

    function FavouritesController($rootScope, $state, ParseLinks, AlertService, pagingParams, Favourites, paginationConstants) {
        var vm = this;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.favourites = null;
        vm.isAuthenticated = null;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.setFavourites = setFavourites;

        loadAll();

        $rootScope.$on('genreSelected', function (event, data) {
            if(data.id == -1){
                loadAll();
            }else {
                Favourites.getCurrentUserFavouritesAndGenre({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    id: data.id
                }, onReceiveMovies);
            }
            function onReceiveMovies(data, headers){
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.favourites = data;
                vm.page = pagingParams.page;
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') result.push('id');
                return result;
            }
        });

        function loadAll() {
            Favourites.getCurrentUserFavourites({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') result.push('id');
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.favourites = data;
                vm.page = pagingParams.page;
                for(var i = 0; i < data.length; i++) data[i].subscribed = true;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')
            });
        }
        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }
        function setFavourites(movie) {
            Favourites.removeCurrentFollower({movieID: movie.id});
            movie.subscribed = false;
            vm.favourites.splice(vm.favourites.indexOf(movie), 1);
        }
    }
})();
