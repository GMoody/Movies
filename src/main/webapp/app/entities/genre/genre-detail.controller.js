(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('GenreDetailController', GenreDetailController);

    GenreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Genre', 'Movie'];

    function GenreDetailController($scope, $rootScope, $stateParams, previousState, entity, Genre, Movie) {
        var vm = this;

        vm.genre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:genreUpdate', function(event, result) {
            vm.genre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
