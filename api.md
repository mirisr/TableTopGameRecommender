#API Docs

---

### Categories

- GET /categories - all
- GET /categories/{category} - get
- GET /categories/{category}/games - get all games in category

### Games
- GET /games - all
- GET /games/{game} - get
- GET /categories/{category}/games - get all games in category
- GET /profiles/{profile}/games - get games for the profile
- POST /profiles/{profile}/games - add to profile's games

### Profiles
- GET /profiles - all
- GET /profiles/{profile} - get
- GET /profiles/{profiles}/games - get recommended games for the profile
- POST /profiles - create
- PUT /profiles/{profile} - update
- DELETE /profiles/{profile} - destroy

### Search
- GET /search - categories, games, sub-categories
