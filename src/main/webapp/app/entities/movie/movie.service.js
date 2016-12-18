(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Movie', Movie);

    Movie.$inject = ['$resource'];

    function Movie ($resource) {
        var resourceUrl =  'api/movies/:id';

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
            'getByGenre': {
                url: '/api/movies/genres/:id',
                method: 'GET',
                params:{
                    id:'@id'
                },
                isArray: true
            }
        });
    }
})();
