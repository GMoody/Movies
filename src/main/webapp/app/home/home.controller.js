(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Movie', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal', 'LoginService', 'Favourites', '$rootScope'];

    function HomeController($scope, $state, Movie, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, LoginService, Favourites, $rootScope) {
        var vm = this;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.login = LoginService.open;

        vm.account = null;
        vm.userMovies = null;
        vm.isAuthenticated = null;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.register = register;
        vm.setFavourites = setFavourites;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        $rootScope.$on('genreSelected', function (event, data) {
            if(data.id == -1){
                loadAll();
            }else {
                Movie.getMoviesByGenre({
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
                vm.movies = data;
                vm.page = pagingParams.page;
                makeSubscribers();
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') result.push('id');
                return result;
            }
        });

        loadAll();
        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                if(vm.account != null) Favourites.getCurrentUserFavourites({}, onReceive);
                function onReceive(movies) {
                    vm.userMovies = movies;
                    makeSubscribers();
                }
            });
        }
        function register() {
            $state.go('register');
        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')
            });
        }
        function loadAll() {
            Movie.query({
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
                vm.movies = data;
                vm.page = pagingParams.page;
                makeSubscribers();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function setFavourites(movie, isFavourite) {
            if(isFavourite){
                Favourites.addCurrentFollower({movieID: movie.id});
                movie.subscribed = true;
                vm.userMovies.push(movie);
            } else {
                Favourites.removeCurrentFollower({movieID: movie.id});
                movie.subscribed = false;
                vm.userMovies.splice(vm.userMovies.indexOf(movie), 1);
            }
        }
        function makeSubscribers() {
            var userMovies = [];
            for(var i = 0; i < vm.userMovies.length; i++)
                userMovies.push(vm.userMovies[i].id);
            for(i = 0; i < vm.movies.length; i++)
                vm.movies[i].subscribed = userMovies.indexOf(vm.movies[i].id) != -1;
        }
    }
})();
