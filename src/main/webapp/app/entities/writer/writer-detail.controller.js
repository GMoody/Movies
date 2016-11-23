(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('WriterDetailController', WriterDetailController);

    WriterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Writer', 'Movie'];

    function WriterDetailController($scope, $rootScope, $stateParams, previousState, entity, Writer, Movie) {
        var vm = this;

        vm.writer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:writerUpdate', function(event, result) {
            vm.writer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
