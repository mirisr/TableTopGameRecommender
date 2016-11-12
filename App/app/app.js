'use strict';

// Declare app level module which depends on views, and components
angular.module('recommender', [
  'ngRoute',
  'recommender.view1',
  'recommender.view2',
  'recommender.version',
  'recommender.server'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/view1'});
}]);
