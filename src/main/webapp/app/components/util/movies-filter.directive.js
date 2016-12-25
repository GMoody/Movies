(function() {
    'use strict';

    var moviesFilter = {
        template:
        '<div class="toolbox">' +
            '<div class="form-group">' +
                '<label data-translate="global.labels.genre" for="genres">Genre</label>' +
                '<select class="form-control genres" id="genres" name="genres"' +
                    'ng-model="$ctrl.selectedGenre"' +
                    'ng-options="genre as genre.title for genre in $ctrl.genres track by genre.id"' +
                    'ng-change="$ctrl.genreSelected()"></select>' +
            '</div>' +
        '</div>',
        controller: MoviesFilterController
    };

    angular
        .module('moviesApp')
        .component('moviesFilter', moviesFilter);

    MoviesFilterController.$inject = ['Genre', '$rootScope'];

    function MoviesFilterController (Genre, $rootScope) {
        var vm = this;

        vm.selectedGenre = null;

        vm.genreSelected = genreSelected;
        vm.loadGenres = loadGenres;

        loadGenres();

        function loadGenres() {
            Genre.getUsed({}, onReceiveGenres);
            function onReceiveGenres(data){
                vm.genres = [];
                vm.genres.push({id: -1, title: "All"});
                vm.genres = vm.genres.concat(data);
            }
        }
        function genreSelected() {
            $rootScope.$broadcast("genreSelected", vm.selectedGenre);
        }
    }
})();
