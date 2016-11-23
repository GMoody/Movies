(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Movie', 'Director', 'Writer', 'Genre', 'Actor', 'Country'];

    function MovieDetailController($scope, $rootScope, $stateParams, previousState, entity, Movie, Director, Writer, Genre, Actor, Country) {
        var vm = this;

        vm.movie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:movieUpdate', function(event, result) {
            vm.movie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
