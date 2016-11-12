package Models;

import java.util.Iterator;
import java.util.List;

public class Game {
	
	public int id;
	public String title;
	public int year;
	public float avg_rating;
	public int no_ratings;
	public float complexity;
	public int min_players;
	public int max_players;
	public int min_time;
	public int max_time;
	public List<String>categories;

	public Game(int id, String title, int year, float avg_rating, int no_ratings,
			float complexity, int min_players, int max_players,
			int min_time, int max_time, List<String> categories) {
		
		this.id = id;
		this.title = title;
		this.year = year;
		this.avg_rating = avg_rating;
		this.no_ratings = no_ratings;
		this.complexity = complexity;
		this.min_players = min_players;
		this.max_players = max_players;
		this.min_time = min_time;
		this.max_time = max_time;
		this.categories = categories;
	}

	public Game() {
		
	}
	
	public String toString() {
		String categories = "";
		for(Iterator<String> i = this.categories.iterator(); i.hasNext(); ) {
		    String item = i.next();
		    categories += item + " ";
		}
	
		return "\n"+title + " " + year + "\nAvg.Rating: " + this.avg_rating
				+ "\nNo. ratings: " + no_ratings + "\nComplexity: " + complexity
				+ "\nPlayers: " + this.min_players + "-" + this.max_players
				+ "\nPlay Time: " + this.min_time + "-" + this.max_time
				+ "\nCategories: " + categories;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!Game.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final Game other = (Game) obj;
	    if (this.id != other.id) {
	        return false;
	    }
	    
	    return true;
	}
}