'use strict';

angular.module('recommender.profile', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/profile', {
    templateUrl: 'profile/profile.html',
    controller: 'profile'
  });
}])

.controller('profile', function($scope, Profile) {

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
          alert("creating profile");
          var newProfile = new Profile();
          newProfile.name = this.fullName();
          newProfile.username = $scope.user.username;
          newProfile.password = $scope.user.password;
          newProfile.email = $scope.user.email;
          //newProfile.baseGame = $scope.user.baseGame;
          //newProfile.$save();
          console.log(newProfile);
      }
    };
});
