(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('WriterDialogController', WriterDialogController);

    WriterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Writer', 'Movie'];

    function WriterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Writer, Movie) {
        var vm = this;

        vm.writer = entity;
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
            if (vm.writer.id !== null) {
                Writer.update(vm.writer, onSaveSuccess, onSaveError);
            } else {
                Writer.save(vm.writer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moviesApp:writerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
