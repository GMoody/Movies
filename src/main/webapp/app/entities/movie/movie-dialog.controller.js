(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('MovieDialogController', MovieDialogController);

    MovieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Movie', 'Director', 'Writer', 'Genre', 'Actor', 'Country'];

    function MovieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Movie, Director, Writer, Genre, Actor, Country) {
        var vm = this;

        vm.movie = entity;
        vm.clear = clear;
        vm.save = save;
        vm.directors = Director.getAll();
        vm.writers = Writer.getAll();
        vm.genres = Genre.getAll();
        vm.actors = Actor.getAll();
        vm.countries = Country.getAll();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.movie.id !== null) {
                Movie.update(vm.movie, onSaveSuccess, onSaveError);
            } else {
                Movie.save(vm.movie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moviesApp:movieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
