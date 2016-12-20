(function() {
    'use strict';

    angular
        .module('moviesApp')
        .directive('avatars', avatars);

    function avatars () {
        var directive = {
            restrict: 'E',
            template:
            '<div class="avatar-div" ng-switch="hasAvatar">' +
                '<img class="avatar" ng-switch-when="defaultFalse" src="content/images/avatar.png"/>' +
                '<img class="avatar" ng-switch-when="defaultTrue" ng-src="{{avatarURL}}" />' +
                '<img class="avatar" ng-switch-when="movie"/>' +
            '</div>',
            scope: {
                item: '=',
                dirclass: '@'
            },
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, iElement) {
            if(scope.item == null || scope.item.avatarURL == null || scope.item.avatarURL.length == 0)
                if(scope.dirclass == "movie"){
                    scope.hasAvatar = "movie";
                    document.querySelector('dd').remove();
                    iElement.remove();
                }else scope.hasAvatar = "defaultFalse";
            else {
                scope.hasAvatar = "defaultTrue";
                scope.avatarURL = scope.item.avatarURL;
            }

            if(scope.dirclass == "settings"){
                iElement.children().first().removeClass('avatar-div');
                iElement.children().first().addClass('user-settings-avatar');
            }

            if(scope.dirclass == "movie"){
                iElement.children().first().removeClass('avatar-div');
                iElement.children().first().addClass('movie-poster');
            }
        }
    }
})();
