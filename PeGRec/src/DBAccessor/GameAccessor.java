package DBAccessor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Computation.GameRanker;
import Models.COF;
import Models.CandidateGame;
import Models.Game;

public class GameAccessor {

	private DB db;
	
	public GameAccessor() {
		db = new DB();
	}
	
	public List<Game> GetProfileGames(int userId) {
		List<Game> profileGames = null;
		PreparedStatement stmt = null;
		try {
			String query = "select * from games g where g.id in (select game_id from profiles where user_id = ?);";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			profileGames = CreateGames(rs);
			for(Iterator<Game> i = profileGames.iterator(); i.hasNext(); ) {
			    Game game = i.next();
			    query = "select distinct(category) from categories where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddCategoriesToGame(rs, game);
				
				query = "select distinct(mechanic) from mechanics where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddMechanicsToGame(rs, game);
				//System.out.println("Finished adding mechanics.");
				
			    query = "select distinct(type) from types where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddTypesToGame(rs, game);
				
			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		
		return profileGames;
	}
	
	public void RemoveGameFromProfile(int userId, int gameId) {
		
		PreparedStatement preparedStmt = null;
		try {
				String query = "delete from profiles where user_id = ? and game_id = ?";
				preparedStmt = db.connection.prepareStatement(query);
				preparedStmt.setInt (1, userId);
				preparedStmt.setInt (2, gameId);
				preparedStmt.execute();
				db.safeClose(preparedStmt);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(preparedStmt);
			
		}
	}
	
	public void AddGameToProfile(int userId, int gameId) {
		
		PreparedStatement preparedStmt = null;
		try {
				String query = "insert into profiles values (?, ?)";
				preparedStmt = db.connection.prepareStatement(query);
				preparedStmt.setInt (1, userId);
				preparedStmt.setInt (2, gameId);
				preparedStmt.execute();
				db.safeClose(preparedStmt);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(preparedStmt);
			
		}
	}
	
	// Creates User into Database - Assumes the user does not already exist in the DB
	// If successful returns the newly created userID. else return -1
	public int CreateUser(String username, String password) {
		int userId = -1;
		PreparedStatement preparedStmt = null;
		try {
				String query = "insert into users (username, password) values (?, ?)";
				preparedStmt = db.connection.prepareStatement(query);
				preparedStmt.setString (1, username);
				preparedStmt.setString (2, password);
				preparedStmt.execute();
				db.safeClose(preparedStmt);
		      
		        query = "select * from users where username = ?";
		        preparedStmt = db.connection.prepareStatement(query);
		        preparedStmt.setString(1, username);
				ResultSet rs = preparedStmt.executeQuery();
				if (rs.next()) {
					userId = rs.getInt("id");
				}
		      
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(preparedStmt);
		}
		return userId;
	}
	
	// Check to see if the User Exists
	// If user exists, return userID
	// else return -1
	public int UserExists(String username) {
		int userId = -1;
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from users where username = ?";
			stmt = db.connection.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("id");
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		return userId;
	}
	
	// Verifies user credentials
	// If the user credentials are successful return userID
	// else return -1
	public int VerifyUser(String username, String password) {
		int userId = -1;
		PreparedStatement stmt = null;
		try {			
			String query = "select * from users where username = ? and password = ?";
			stmt = db.connection.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2,  password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("id");
			}
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		return userId;
	}
	
	public List<CandidateGame> GetCandidateGames(List<Game>profile, Game designatedGame) {
		List<Game> allGames = GetAllGames();
		List<CandidateGame> candidateGames = new ArrayList<CandidateGame>();
		
		// Remove Games that are in the profile and if it's the designated Game
		allGames.remove(designatedGame);
		for(Iterator<Game> i = profile.iterator(); i.hasNext(); ) {
		    Game game = i.next();
		    allGames.remove(game);
		}
		// Create a list of Candidate Game objects
		for(Iterator<Game> i = allGames.iterator(); i.hasNext(); ) {
		    Game game = i.next();
		    CandidateGame candidate = new CandidateGame();
		    candidate.game = game;
		    candidateGames.add(candidate);
		}
		
		return candidateGames;
	}
	
	public List<CandidateGame> GetTopN(int userId, int desId, int n) {
		List<Game> profileGames = null;
		PreparedStatement stmt = null;
		try {
			String query = "select * from games g where g.id in (select game_id from profiles where user_id = ?);";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			profileGames = CreateGames(rs);
			Game desGame = null;
			for(Iterator<Game> i = profileGames.iterator(); i.hasNext(); ) {
			    Game game = i.next();
			    query = "select distinct(category) from categories where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddCategoriesToGame(rs, game);
				
				query = "select distinct(mechanic) from mechanics where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddMechanicsToGame(rs, game);
				//System.out.println("Finished adding mechanics.");
				
			    query = "select distinct(type) from types where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddTypesToGame(rs, game);
				
				if(game.id == desId) {
					desGame = game;
				}
			}

			if (desGame == null) {
				query = "select * from games where id = ?;";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, desId);
				rs = stmt.executeQuery();
				desGame = CreateGames(rs).get(0);
			}


			
			GameRanker gr = new GameRanker(this, profileGames, desGame);
			return gr.GetTopNGames(n);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		
		return null;
	}

	/**
	 * Eager loads everything except description and image_link for the 5000 top ranked games.
	 * This is for SQL optimization - takes this procedure from over a minute to a second.
	 * Returned to front end, if user clicks on a game lazy load the rest of it.
	 * @return mixed
	 */
	public List<Game> GetAllGames() {
		List<Game>games = null;
		PreparedStatement stmt = null;
		try {
			//order by gbg_overall_rank
			String query = "select * from games order by gbg_overall_rank limit 5000";
			stmt = db.connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			games = CreateGames(rs);
			db.safeClose(stmt);

			for(Iterator<Game> i = games.iterator(); i.hasNext(); ) {
				Game game = i.next();
				query = "select distinct(category) from categories where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddCategoriesToGame(rs, game);
				//System.out.println("Finished adding categories.");

				query = "select distinct(mechanic) from mechanics where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddMechanicsToGame(rs, game);
				//System.out.println("Finished adding mechanics.");

				query = "select distinct(type) from types where id = ? ";
				stmt = db.connection.prepareStatement(query);
				stmt.setInt(1, game.id);
				rs = stmt.executeQuery();
				game = AddTypesToGame(rs, game);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		
		return games;
	}
	
	public Game GetGameByName(String name) {
		Game game = null;
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from games where title = ? ";
			stmt = db.connection.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			game = CreateGames(rs).get(0);
			db.safeClose(stmt);
			
			query = "select distinct(category) from categories where id = ? ";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, game.id);
			rs = stmt.executeQuery();
			game = AddCategoriesToGame(rs, game);
			
			query = "select distinct(mechanic) from mechanics where id = ? ";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, game.id);
			rs = stmt.executeQuery();
			game = AddMechanicsToGame(rs, game);
			//System.out.println("Finished adding mechanics.");
			
		    query = "select distinct(type) from types where id = ? ";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, game.id);
			rs = stmt.executeQuery();
			game = AddTypesToGame(rs, game);
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			db.safeClose(stmt);
		}
		return game;
	}
	
	public List<COF> GetCOFs() {
		List<COF> cofs = new ArrayList<COF>();
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from cof";
			stmt = db.connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				COF cof = new COF();
				cof.category1= rs.getString("category1");
				cof.category2 = rs.getString("category2");
				cof.cof = rs.getFloat("cof");
				cofs.add(cof);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.safeClose(stmt);
		}
		return cofs;
		
	}
	
	public List<COF> GetMCOFs() {
		List<COF> cofs = new ArrayList<COF>();
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from mcof";
			stmt = db.connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				COF cof = new COF();
				cof.category1= rs.getString("mech1");
				cof.category2 = rs.getString("mech2");
				cof.cof = rs.getFloat("cof");
				cofs.add(cof);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.safeClose(stmt);
		}
		return cofs;
		
	}
	
	public List<COF> GetTCOFs() {
		List<COF> cofs = new ArrayList<COF>();
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from tcof";
			stmt = db.connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				COF cof = new COF();
				cof.category1= rs.getString("type1");
				cof.category2 = rs.getString("type2");
				cof.cof = rs.getFloat("cof");
				cofs.add(cof);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.safeClose(stmt);
		}
		return cofs;
		
	}
 	
	private List<Game> CreateGames(ResultSet rs) {
		List<Game> games = new ArrayList<Game>();
		try {
			
			while(rs.next()) {
				Game game = new Game();
				game.id = rs.getInt("id");
				game.title = rs.getString("title");
				game.year = rs.getInt("year");
				game.avg_rating = rs.getFloat("avg_rating");
				game.no_ratings = rs.getInt("no_ratings");
				game.complexity = rs.getFloat("complexity");
				game.min_players = rs.getInt("min_players");
				game.max_players = rs.getInt("max_players");
				game.min_time = rs.getInt("min_time");
				game.max_time = rs.getInt("max_time");
				game.description = rs.getString("description");
				game.image_link = rs.getString("image_link");
				
				games.add(game);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return games;
	}

	/**
	 * Skinny games don't have description or image link, since those take too long to be indexed.
	 * These fields must be loaded before being returned to the client.
	 * @param rs
	 * @return
	 *//*
	private List<Game> CreateSkinnyGames(ResultSet rs) {
		List<Game> games = new ArrayList<Game>();
		try {

			while(rs.next()) {
				Game game = new Game();
				game.id = rs.getInt("id");
				game.title = rs.getString("title");
				game.avg_rating = rs.getFloat("avg_rating");
				game.id = rs.getInt("id");
				game.title = rs.getString("title");
				game.year = rs.getInt("year");
				game.avg_rating = rs.getFloat("avg_rating");
				game.no_ratings = rs.getInt("no_ratings");
				game.complexity = rs.getFloat("complexity");
				game.min_players = rs.getInt("min_players");
				game.max_players = rs.getInt("max_players");
				game.min_time = rs.getInt("min_time");
				game.max_time = rs.getInt("max_time");

				games.add(game);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return games;
	}
*/
	/**
	 * Loads description and image_link and adds them to the game
	 * @param game the game
	 */
	public void LazyLoadGame(Game game) {

		PreparedStatement stmt = null;
		try {
			String query = "select description, image_link from games where id = ? ";
			stmt = db.connection.prepareStatement(query);
			stmt.setInt(1, game.id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				game.description = rs.getString("description");
				game.image_link = rs.getString("image_link");
			}
			db.safeClose(stmt);
		}
		catch (Exception e) {
			db.safeClose(stmt);
		}
	}
	
	private Game AddCategoriesToGame(ResultSet rs, Game game) {
		List<String>categories = new ArrayList<String>();
		try {
			
			while(rs.next()) {
				categories.add(rs.getString("category"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		game.categories = categories;
		return game;
	}
	
	private Game AddMechanicsToGame(ResultSet rs, Game game) {
		List<String>mechanics = new ArrayList<String>();
		try {
			
			while(rs.next()) {
				mechanics.add(rs.getString("mechanic"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		game.mechanics = mechanics;
		return game;
	}
	
	private Game AddTypesToGame(ResultSet rs, Game game) {
		List<String>types = new ArrayList<String>();
		try {
			
			while(rs.next()) {
				types.add(rs.getString("type"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		game.types = types;
		return game;
	}
}
