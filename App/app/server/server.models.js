angular.module('myApp.server')

.factory('Profile', function($resource) {
    return $resource('/api/profiles/:id', {}, {
        games: {
          url: /api/profiles/:id/games,
          method: GET,
          isArray: true
        },
        addGame: {
          url: /api/profiles/:id/games,
          method: POST
        },
        removeGame: {
          url: /api/profiles/:id/games,
          method: DELETE
        }
    });
})

.factory('Category', function($resource) {
    return $resource('/api/categories/:id', {}, {
        games: {
          url: /api/categories/:id/games,
          method: GET,
          isArray: true
        }
    });
})

.factory('Game', function($resource) {
    return $resource('/api/games/:id', {}, {

    });
})
