(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Director', Director);

    Director.$inject = ['$resource'];

    function Director ($resource) {
        var resourceUrl =  'api/directors/:id';
        var getDirectorMovies = 'api/directors/:id/movies';

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
                url: 'api/directors/all',
                isArray: true
            },
            'getDirectorMovies': {
                method: 'GET',
                url: getDirectorMovies,
                isArray: true,
                params: {
                    id: '@id'
                }
            }
        });
    }
})();
