#API Docs

---

### Categories

- GET /categories - all
- GET /categories/{category} - get
- GET /categories/{category}/games - get all games in category

### Games
- GET /games-all/ - all (IMPLEMENTED)
- GET /games/{game} - get
- GET /categories/{category}/games - get all games in category
- GET /profiles-games-add/{userId}/{gameId} - get games for the profile (IMPLEMENTED)
- POST /profiles-games-all/{userId} - add to profile's games (IMPLEMENTED)
- GET /games-top-n/{userId}/{designatedGameId}/{n} - (IMPLEMENTED) returns top n recommendations based on user ID and designated Game ID

### Profiles
- GET /profiles - all
- GET /profiles/{profile} - get
- GET /profiles/{profiles}/games - get recommended games for the profile
- POST /profiles-create/{username}/{password} - create (IMPLEMENTED)
- PUT /profiles/{profile} - update
- DELETE /profiles-games-remove/{userId}/{gameId} - destroy (IMPLEMENTED)
- GET /profiles-verify/{username}/{password} - returns -1 if not found, otherwise the user ID(IMPLEMENTED)
- GET /profiles-find/{username} - returns -1 if username is not found, other the userID (IMPLEMENTED)

### Search
- GET /search - categories, games, sub-categories
