(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('DirectorDetailController', DirectorDetailController);

    DirectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Director', '$window'];

    function DirectorDetailController($scope, $rootScope, $stateParams, previousState, entity, Director, $window) {
        var vm = this;

        vm.director = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:directorUpdate', function(event, result) {
            vm.director = result;
            $window.location.reload();
        });
        $scope.$on('$destroy', unsubscribe);

        getDirectorMovies();

        function getDirectorMovies() {
            Director.getDirectorMovies({id: vm.director.id}, onSuccess);
            function onSuccess(data) {
                vm.director.movies = data;
            }
        }
    }
})();
