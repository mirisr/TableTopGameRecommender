'use strict';

angular.module('recommender.game', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/game', {
    templateUrl: 'game/game.html',
    controller: 'game'
  });
}])

.controller('game', [function() {

}]);
