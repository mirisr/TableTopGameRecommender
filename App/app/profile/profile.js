'use strict';

angular.module('recommender.profile', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/profile', {
    templateUrl: 'profile/profile.html',
    controller: 'profile'
  });
}])

.controller('profile', function($scope, Profile, User) {

    $scope.user = {
      firstName: "",
      lastName: "",
      password: "",
      email: "",
      //baseGame: "",

      fullName: function() {
        return $scope.user.firstName + ' ' + $scope.user.lastName;
      },


      createProfile: function() {
          Profile.createUser($scope.user.username, $scope.user.password)
              .then(function success(response) {
                  User.id = response.data.userId;
                  // move on to the designated game page
              },
              function error(response){
                  console.log(response);
                  // tell user there was an error
              });
      },

      test: function() {
        Profile.getTopNGames(8, 1, 20)
            .then(function(response) {
              console.log(response);
            }, function(response) {
                console.log(response);
            });
      }
    };
});
