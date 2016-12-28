(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity', '$window'];

    function MovieDetailController($scope, $rootScope, previousState, entity, $window) {
        var vm = this;

        vm.movie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:movieUpdate', function(event, result) {
            vm.movie = result;
            $window.location.reload();
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
