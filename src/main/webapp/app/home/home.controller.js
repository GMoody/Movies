(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Movie', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal', 'LoginService', 'MovieFollowers', 'User'];

    function HomeController($scope, $state, Movie, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, LoginService, MovieFollowers, User) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.account = null;
        vm.userMovies = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.checkMovie = checkMovie;

        // Buttons actions
        vm.addFollower = addFollower;
        vm.removeFollower = removeFollower;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();
        loadAll();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                User.getUserMoviesByLogin({login: vm.account.login}, onReceive);
                function onReceive(movies) {
                    vm.userMovies = movies;
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
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
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
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.movies = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function addFollower(movie) {
            MovieFollowers.addFollower({movieID: movie.id, userLogin: vm.account.login});
            $state.reload();
        }

        function removeFollower(movie) {
            MovieFollowers.removeFollower({movieID: movie.id, userLogin: vm.account.login});
            $state.reload();
        }

        function checkMovie(movie) {
            if(vm.userMovies == null) return false;
            else {
                for(var i = 0; i < vm.userMovies.length; i++){
                    if(vm.userMovies[i].id == movie.id)
                        return true;
                }
                return false;
            }
        }
    }
})();
