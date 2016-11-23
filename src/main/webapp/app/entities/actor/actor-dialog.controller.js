(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('ActorDialogController', ActorDialogController);

    ActorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Actor', 'Movie'];

    function ActorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Actor, Movie) {
        var vm = this;

        vm.actor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.movies = Movie.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.actor.id !== null) {
                Actor.update(vm.actor, onSaveSuccess, onSaveError);
            } else {
                Actor.save(vm.actor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moviesApp:actorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
