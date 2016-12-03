(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('MovieFollowers', MovieFollowers);

    MovieFollowers.$inject = ['$resource'];

    function MovieFollowers($resource) {
        var resourceUrl =  'api/movies/followers/';

        return $resource(resourceUrl, {}, {
            'addFollower': {
                url: '/api/movies/followers/:movieID/:userLogin',
                method: 'POST',
                params:{
                    movieID:'@movieID',
                    userLogin: '@userLogin'
                }
            },
            'removeFollower': {
                url: '/api/movies/followers/:movieID/:userLogin',
                method: 'DELETE',
                params:{
                    movieID:'@movieID',
                    userLogin: '@userLogin'
                }
            },
            'getMovieFollowers': {
                url: '/api/movies/:id/followers/',
                params:{
                    id: "@id"
                },
                method: 'GET',
                isArray: true
            }
        })
    }
})();
