(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Movie', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal', 'LoginService', 'Favourites', 'Genre', '$translate', '$rootScope'];

    function HomeController($scope, $state, Movie, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, LoginService, Favourites, Genre, $translate, $rootScope) {
        var vm = this;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.login = LoginService.open;

        vm.account = null;
        vm.userMovies = null;
        vm.isAuthenticated = null;
        vm.selectedGenre = null;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.register = register;
        vm.checkMovie = checkMovie;
        vm.addFollower = addFollower;
        vm.removeFollower = removeFollower;
        vm.genreSelected = genreSelected;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();
        loadAll();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                if(vm.account != null) Favourites.getCurrentUserFavourites({}, onReceive);
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
                if (vm.predicate !== 'id') result.push('id');
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
            Genre.getAll({}, onReceiveGenres, onError);
            function onReceiveGenres(data){
                data.sort(function(a,b) {return (a.title > b.title) ? 1 : ((b.title > a.title) ? -1 : 0);} );
                vm.genres = [];
                vm.genres.push({id: -1, title: "All"});
                vm.genres = vm.genres.concat(data);
            }
        }
        function addFollower(movie) {
            Favourites.addCurrentFollower({movieID: movie.id});
            $state.reload();
        }
        function removeFollower(movie) {
            Favourites.removeCurrentFollower({movieID: movie.id});
            $state.reload();
        }
        function checkMovie(movie) {
            if(vm.userMovies == null) return false;
            else for(var i = 0; i < vm.userMovies.length; i++)
                    if(vm.userMovies[i].id == movie.id)
                        return true;
                return false;
        }
        function genreSelected() {
            if(vm.selectedGenre.id == -1){
                loadAll();
                elementsVisibility(true);
            }else {
                Movie.getByGenre({id: vm.selectedGenre.id}, onReceiveMovies);
                elementsVisibility(false);
            }
            function onReceiveMovies(data){
                vm.movies = data;
            }
        }
        function elementsVisibility(visible) {
            var counter = document.getElementById("jhi-item-count");
            var glyphicons = document.getElementsByClassName("glyphicon glyphicon-sort");
            var glyphicons2 = document.getElementsByClassName("glyphicon glyphicon-sort-by-attributes");
            var pager = document.getElementById("pager");

            if(visible){
                if(glyphicons2.length != 0) glyphicons2[0].style.visibility = "visible";
                counter.style.visibility = "visible";
                pager.style.visibility = "visible";
                vm.transition = transition;
                for(var i = 0; i < glyphicons.length; i++)
                    glyphicons[i].style.visibility = "visible";
            } else {
                if(glyphicons2.length != 0) glyphicons2[0].style.visibility = "hidden";
                counter.style.visibility = "hidden";
                pager.style.visibility = "hidden";
                vm.transition = null;
                for(var y = 0; y < glyphicons.length; y++)
                    glyphicons[y].style.visibility = "hidden";
            }
        }
    }
})();
