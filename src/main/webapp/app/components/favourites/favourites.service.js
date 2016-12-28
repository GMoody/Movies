(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Favourites', Favourites);

    Favourites.$inject = ['$resource'];

    function Favourites($resource) {
        var resourceUrl =  'api/movies/followers/';
        var addFollower = 'api/movies/:movieID/followers';
        var removeFollower = 'api/movies/:movieID/followers';
        var getMovieFollowers = 'api/movies/:id/followers';
        var getCurrentUserFavourites = 'api/users/current/favourites';
        var getCurrentUserFavouritesAndGenre = 'api/users/current/favourites/genres/:id';

        return $resource(resourceUrl, {}, {
            'addCurrentFollower': {
                url: addFollower,
                method: 'POST',
                params:{
                    movieID:'@movieID'
                }
            },
            'removeCurrentFollower': {
                url: removeFollower,
                method: 'DELETE',
                params:{
                    movieID:'@movieID'
                }
            },
            'getMovieFollowers': {
                url: getMovieFollowers,
                method: 'GET',
                isArray: true,
                params:{
                    id: "@id"
                }
            },
            'getCurrentUserFavourites': {
                url: getCurrentUserFavourites,
                method: 'GET',
                isArray: true
            },
            'getCurrentUserFavouritesAndGenre': {
                url: getCurrentUserFavouritesAndGenre,
                method: 'GET',
                isArray: true,
                params:{
                    id: "@id"
                }
            }
        })
    }
})();
