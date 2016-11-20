'use strict';

angular.module('recommender.game', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/game', {
    templateUrl: '../assets/DisplayRecs.html',
    controller: 'game'
  });
}])

.controller('game', [function() {

}]);
