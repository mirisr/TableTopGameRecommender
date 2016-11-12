angular.module('recommender.server')

.factory('OldProfile', function($resource) {
    return $resource('/api/profiles/:id', {}, {
        games: {
          url: '/api/profiles/:id/games',
          method: 'GET',
          isArray: true
        },
        addGame: {
          url: '/api/profiles/:id/games',
          method: 'POST'
        },
        removeGame: {
          url: '/api/profiles/:id/games',
          method: 'DELETE'
        }
    });
})

.factory('Category', function($resource) {
    return $resource('/api/categories/:id', {}, {
        games: {
          url: '/api/categories/:id/games',
          method: 'GET',
          isArray: true
        }
    });
})

.factory('Game', function($resource) {
    return $resource('/api/games/:id', {}, {

    });
})

// Server communicators for NON-rest API
.factory('Profile', function($resource) {
    return {

        // creates new user with username and password
        // returns promise: id of newly created user
        // /profiles-create/user/{username}/{password}
        createUser: function(username, password) {
            return $http.get('/profiles-create/user/' + username + '/' + password, {});
        },

        // verifies username and password on login
        // returns promise: user ID or -1 if invalid
        // /profiles-verify/user/{username}/{password}
        verifyUser: function(username, password) {
            return $http.get('/profiles-verify/user/' + username + '/' + password, {});
        },

        // verifies username exists without password?
        // returns promise: user ID or -1 if invalid
        // /profiles-find/user/{username}
        findUser: function(username) {
            return $http.get('/profiles-find/user' + username, {});
        },

        // add game to profile using  user id and game id
        // returns promise: Success
        // /profiles-games-add/{user_id}/{game_id}
        addGame: function(userId, gameId) {
            return $http.get('/profiles-games-add/' + userId + '/' + gameId, {});
        },

        // remove game from profile
        removeGame: function() {

        },

        // get n games associated with profile, using profile's designated game,
        // user id
        // returns promise:  JSON and *pray* that it's formatted correctly
        // /games-top-n/top_n/{user_id}/{des_game}/{n}
        getTopNGames: function(userId, designatedGame, number) {
            return $http.get('/games-top-n/top_n/' + userId + '/' + designatedGame + '/' + number);
        },

        // get all games associated with profile
        // returns promise: JSON of array of games
        // /profiles-games/{user_id}
        getGames: function(userId) {
            return $http.get('profiles-games/' + userId);
        };
    }
})
