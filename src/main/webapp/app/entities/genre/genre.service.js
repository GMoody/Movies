(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Genre', Genre);

    Genre.$inject = ['$resource'];

    function Genre ($resource) {
        var resourceUrl =  'api/genres/:id';

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
                url: 'api/genres/all',
                isArray: true
            },
            'getUsed': {
                method: 'GET',
                url: 'api/genres/used/all',
                isArray: true
            }
        });
    }
})();
