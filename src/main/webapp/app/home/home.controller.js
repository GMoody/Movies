(function () {
    'use strict';

    angular
        .module('moviesApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Movie', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal', 'LoginService', 'Favourites', 'Genre'];

    function HomeController($scope, $state, Movie, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, LoginService, Favourites, Genre) {
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
        vm.checkMovie = checkMovie;
        vm.addFollower = addFollower;
        vm.removeFollower = removeFollower;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();
        setupDDM();
        loadAll();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                if(vm.account != null){
                    Favourites.getCurrentUserFavourites({}, onReceive);
                }

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

            Genre.getAll({}, onReceiveGenres, onError);
            function onReceiveGenres(data){
                for(var i = 0; i < data.length; i++){
                    $scope.DDMdata.push({id: data[i].id, title: data[i].title});
                }
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
            else {
                for(var i = 0; i < vm.userMovies.length; i++){
                    if(vm.userMovies[i].id == movie.id)
                        return true;
                }
                return false;
            }
        }

        function setupDDM() {
            $scope.DDMlabel = { buttonDefaultText: 'Genres' };
            $scope.DDMmodel = [];
            $scope.DDMdata = [];

            if($(window).width() < 768){
                $scope.DDMsettings = {
                    displayProp: 'title',
                    smartButtonMaxItems: 0,
                    selectionLimit: 3,
                    scrollableHeight: '150px',
                    scrollable: true,
                    closeOnBlur: true,
                    showUncheckAll: false,
                    enableSearch: false
                };
            }else if($(window).width() > 768 && $(window).width() < 991){
                $scope.DDMsettings = {
                    displayProp: 'title',
                    smartButtonMaxItems: 0,
                    selectionLimit: 3,
                    scrollableHeight: '300px',
                    scrollable: true,
                    closeOnBlur: true,
                    showUncheckAll: false,
                    enableSearch: false
                };
            }else {
                $scope.DDMsettings = {
                    displayProp: 'title',
                    smartButtonMaxItems: 3,
                    selectionLimit: 3,
                    scrollableHeight: '300px',
                    scrollable: true,
                    closeOnBlur: true,
                    showUncheckAll: false,
                    enableSearch: true
                };
            }
        }
    }
})();
