'use strict';

// Declare app level module which depends on views, and components
angular.module('recommender', [
  'ngRoute',
  'ngResource',
  'recommender.server',
  'recommender.profile',
  'recommender.game',
  'recommender.version'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/profile'});
}]);
