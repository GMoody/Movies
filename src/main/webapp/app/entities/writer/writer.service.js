(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Writer', Writer);

    Writer.$inject = ['$resource'];

    function Writer ($resource) {
        var resourceUrl =  'api/writers/:id';
        var getWriterMovies = 'api/writers/:id/movies';

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
                url: 'api/writers/all',
                isArray: true
            },
            'getWriterMovies': {
                method: 'GET',
                url: getWriterMovies,
                isArray: true,
                params: {
                    id: '@id'
                }
            }
        });
    }
})();
