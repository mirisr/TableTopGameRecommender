'use strict';

angular.module('recommender.game', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/game', {
    templateUrl: 'game/game.html',
    controller: 'gameCtrl'
  });
}])

.controller('gameCtrl', [function() {

}]);
