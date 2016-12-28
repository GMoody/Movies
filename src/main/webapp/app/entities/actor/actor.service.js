(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Actor', Actor);

    Actor.$inject = ['$resource'];

    function Actor ($resource) {
        var resourceUrl =  'api/actors/:id';
        var getAllActorsURL = "api/actors/all";
        var getActorMovies = 'api/actors/:id/movies';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getAll': {
                method: 'GET',
                url: getAllActorsURL,
                isArray: true
            },
            'getActorMovies': {
                method: 'GET',
                url: getActorMovies,
                isArray: true,
                params: {
                    id: '@id'
                }
            }
        });
    }
})();
