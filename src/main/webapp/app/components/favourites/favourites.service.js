(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Favourites', Favourites);

    Favourites.$inject = ['$resource'];

    function Favourites($resource) {
        var resourceUrl =  'api/movies/followers/';

        return $resource(resourceUrl, {}, {
            'addCurrentFollower': {
                url: '/api/movies/:movieID/followers/add',
                method: 'POST',
                params:{
                    movieID:'@movieID'
                }
            },
            'removeCurrentFollower': {
                url: '/api/movies/:movieID/followers/remove',
                method: 'DELETE',
                params:{
                    movieID:'@movieID'
                }
            },
            'getMovieFollowers': {
                url: '/api/movies/:id/followers/',
                params:{
                    id: "@id"
                },
                method: 'GET',
                isArray: true
            },
            'getCurrentUserFavourites': {
                url: 'api/users/current/favourites',
                method: 'GET',
                isArray: true
            },
            'getCurrentUserFavouritesAndGenre': {
                url: 'api/users/current/favourites/genres/:id',
                params:{
                    id: "@id"
                },
                method: 'GET',
                isArray: true
            }
        })
    }
})();
