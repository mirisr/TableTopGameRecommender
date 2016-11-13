'use strict';

angular.module('recommender.recommended', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/recommended', {
    templateUrl: '../assets/DisplayRecs.html',
    controller: 'recommended'
  });
}])

.controller('recommended', function($scope, Profile) {

      Profile.getTopNGames(8,1,5)
        .then(function(response) {
          console.log(response);
          angular.forEach(response.data.games, function(game) {
              game.image_link = decodeURIComponent(game.image_link);
          });
          $scope.games = response.data.games;
        });
});
