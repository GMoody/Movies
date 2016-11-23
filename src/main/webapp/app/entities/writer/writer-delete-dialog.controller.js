(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('WriterDeleteController',WriterDeleteController);

    WriterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Writer'];

    function WriterDeleteController($uibModalInstance, entity, Writer) {
        var vm = this;

        vm.writer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Writer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
