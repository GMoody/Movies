(function() {
    'use strict';
    angular
        .module('moviesApp')
        .factory('Writer', Writer);

    Writer.$inject = ['$resource'];

    function Writer ($resource) {
        var resourceUrl =  'api/writers/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
