'use strict';

// Declare app level module which depends on views, and components
angular.module('recommender', [
  'ng',
  'ngRoute',
  'ngResource',
  'recommender.server',
  'recommender.profile',
  'recommender.game',
  'recommender.version',
  'recommender.recommended'
])

.constant('HOST', 'http://localhost:8080')

.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/profile'});
}]);
