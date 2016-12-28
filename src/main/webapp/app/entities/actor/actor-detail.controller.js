(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('ActorDetailController', ActorDetailController);

    ActorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Actor', '$window'];

    function ActorDetailController($scope, $rootScope, $stateParams, previousState, entity, Actor, $window) {
        var vm = this;

        vm.actor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moviesApp:actorUpdate', function(event, result) {
            vm.actor = result;
            $window.location.reload();
        });
        $scope.$on('$destroy', unsubscribe);

        getActorMovies();

        function getActorMovies() {
            Actor.getActorMovies({id: vm.actor.id}, onSuccess);
            function onSuccess(data) {
                vm.actor.movies = data;
            }
        }
    }
})();
