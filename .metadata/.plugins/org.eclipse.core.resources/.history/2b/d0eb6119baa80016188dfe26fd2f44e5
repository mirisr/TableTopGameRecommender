package DBAccessor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Models.COF;
import Models.CandidateGame;
import Models.Game;

public class GameAccessor {

	private DB db;
	
	public GameAccessor() {
		db = new DB();
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
	
	private List<Game> GetAllGames() {
		List<Game>games = null;
		PreparedStatement stmt = null;
		try {
			
			String query = "select * from games limit 20000";
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
				games.add(game);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return games;
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
	
	
}
