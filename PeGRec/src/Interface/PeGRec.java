package Interface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Computation.GameRanker;
import DBAccessor.GameAccessor;
import Models.CandidateGame;
import Models.Game;

public class PeGRec {

	private static GameAccessor accessor = new GameAccessor();
	
	public static void main(String[] args) {
		PeGRec rec = new PeGRec();
		List<Game>profile = rec.CreateProfile_Eric_2();
		Game designatedGame = rec.CreateDesignatedGame_Eric_2();
		
		System.out.println("\n\nGenerating Recommendations...\n\n");
		
		GameRanker ranker = new GameRanker(accessor, profile, designatedGame);
		List<CandidateGame> topGames = ranker.GetTopNGames(5);
		System.out.println("-------------------------------");
		for(Iterator<CandidateGame> i = topGames.iterator(); i.hasNext(); ) {
			CandidateGame game = i.next();
			System.out.println(game.toString());
			System.out.println("-------------------------------");
		}
	}
	
    private List<Game> CreateProfile_Eric_2() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
			add("Secret Hitler");
		    add("The Resistance: Avalon");
		    add("Ultimate Werewolf: Ultimate Edition");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    System.out.println(game.toString());
		}
		
		return profile;
	}
	
	private Game CreateDesignatedGame_Eric_2() {
		String name = "Secret Hitler";
		Game game = accessor.GetGameByName(name);
		System.out.println(game.toString());
		return game;
	}
	
	
	private List<Game> CreateProfile_Eric() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
		    add("Betrayal at House on the Hill");
		    add("Specter Ops");
		    add("Smash Up");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    System.out.println(game.toString());
		}
		
		return profile;
	}
	
	private Game CreateDesignatedGame_Eric() {
		String name = "Five Tribes";
		Game game = accessor.GetGameByName(name);
		System.out.println(game.toString());
		return game;
	}
	
	private List<Game> CreateProfile_Brock() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
		    add("Ticket to Ride");
		    add("Carcassonne");
		    add("Guillotine");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    System.out.println(game.toString());
		}
		
		return profile;
	}
	
	private Game CreateDesignatedGame_Brock() {
		String name = "Catan";
		Game game = accessor.GetGameByName(name);
		System.out.println(game.toString());
		return game;
	}
	
	private List<Game> CreateProfile_Kevin() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
		    add("Betrayal at House on the Hill");
		    add("Dead of Winter: A Crossroads Game");
		    add("Smash Up");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    System.out.println(game.toString());
		}
		
		return profile;
	}
	
	private Game CreateDesignatedGame_Kevin() {
		String name = "Betrayal at House on the Hill";
		Game game = accessor.GetGameByName(name);
		System.out.println(game.toString());
		return game;
	}
	
	private List<Game> CreateProfile_Iris() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
		    add("Spot it!");
		    add("Guillotine");
		    add("Cranium");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    System.out.println(game.toString());
		}
		
		return profile;
	}
	
	
	private Game CreateDesignatedGame_Iris() {
		String name = "Catch Phrase!";
		Game game = accessor.GetGameByName(name);
		System.out.println(game.toString());
		return game;
	}
	
	private Game CreateDesignatedGame1() {
		String name = "Pandemic Legacy: Season 1";
		Game game = accessor.GetGameByName(name);
		//System.out.println(game.toString());
		return game;
	}
	
	private List<Game> CreateProfile1() {
		List<Game>profile = new ArrayList<Game>();
		ArrayList<String> gameNames = new ArrayList<String>() {{
		    add("Puerto Rico");
		    add("Agricola");
		    add("Power Grid");
		    add("7 Wonders");
		    add("Eldritch Horror");
		}};
		
		for(Iterator<String> i = gameNames.iterator(); i.hasNext(); ) {
		    String name = i.next();
		    Game game = accessor.GetGameByName(name);
		    profile.add(game);
		    //System.out.println(game.toString());
		}
		
		return profile;
	}
		
	private void TestGetGameByName() {
		System.out.println(accessor.GetGameByName("Twilight Struggle"));
	}

}