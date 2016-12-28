(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('WriterDetailController', WriterDetailController);

    WriterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Writer', '$window'];

    function WriterDetailController($scope, $rootScope, $stateParams, previousState, entity, Writer, $window) {
        var vm = this;

        vm.writer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:writerUpdate', function(event, result) {
            vm.writer = result;
            $window.location.reload();
        });
        $scope.$on('$destroy', unsubscribe);

        getWriterMovies();

        function getWriterMovies() {
            Writer.getWriterMovies({id: vm.writer.id}, onSuccess);
            function onSuccess(data) {
                vm.writer.movies = data;
            }
        }
    }
})();
