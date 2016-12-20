(function() {
    'use strict';

    var moviesFilter = {
        template:
        '<div class="toolbox">' +
            '<div class="form-group">' +
                '<label data-translate="moviesApp.movie.genre" for="genres">Genre</label>' +
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
            Genre.getAll({}, onReceiveGenres);
            function onReceiveGenres(data){
                data.sort(function(a,b) {return (a.title > b.title) ? 1 : ((b.title > a.title) ? -1 : 0);} );
                vm.genres = [];
                vm.genres.push({id: -1, title: "All"});
                vm.genres = vm.genres.concat(data);
            }
        }
        function genreSelected() {
            if(vm.selectedGenre.id == -1) elementsVisibility(true);
            else elementsVisibility(false);
            $rootScope.$broadcast("genreSelected", vm.selectedGenre);
        }
        function elementsVisibility(visible) {
            var glyphicons = document.getElementsByClassName("glyphicon glyphicon-sort");
            var glyphicons2 = document.getElementsByClassName("glyphicon glyphicon-sort-by-attributes");

            if(visible){
                if(glyphicons2.length != 0) glyphicons2[0].style.visibility = "visible";
                for(var i = 0; i < glyphicons.length; i++)
                    glyphicons[i].style.visibility = "visible";
            } else {
                if(glyphicons2.length != 0) glyphicons2[0].style.visibility = "hidden";
                for(var y = 0; y < glyphicons.length; y++)
                    glyphicons[y].style.visibility = "hidden";
            }
        }
    }
})();
