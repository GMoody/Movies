(function() {
    'use strict';

    angular
        .module('moviesApp')
        .controller('JhiConfigurationController', JhiConfigurationController);

    JhiConfigurationController.$inject = ['JhiConfigurationService'];

    function JhiConfigurationController (JhiConfigurationService) {
        var vm = this;

        vm.allConfiguration = null;
        vm.configuration = null;

        JhiConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        JhiConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
