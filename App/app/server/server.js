// Module for all server communication
'use strict';

angular.module('recommender.server', [
    'ngRoute'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/server', {
    templateUrl: 'server/server.html'
  });
}])
