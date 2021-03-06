package Models;

import java.net.URLEncoder;
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
	public List<String> categories;
	public List<String> mechanics;
	public List<String> types;
	public String description;
	public String image_link;
	
	public Game(int id, String title, int year, float avg_rating, int no_ratings,
			float complexity, int min_players, int max_players,
			int min_time, int max_time, List<String> categories, 
			List<String> mechanics, List<String> types,
			String description, String image_link) {
		
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
		this.mechanics = mechanics;
		this.types = types;
		this.description = description;
		this.image_link = image_link;
	}
	
	public Game() {
		
	}
	
	public String GetShortDescription(int length) {
		if (this.description.length() > length) {
			return this.description.substring(0, length) + "...";
		}
		return this.description;
	}
	
	public String GetStringFromList(List<String> list) {
		String str = "";
		for(Iterator<String> i = list.iterator(); i.hasNext(); ) {
		    String item = i.next();
		    str += item + ", ";
		}
		if (str.length() > 1)
			return str.substring(0, str.length()-2);
		return str;
	}
	
	public String toString() {
		
		String categoriesStr = GetStringFromList(this.categories);
		String mechsStr = GetStringFromList(this.mechanics);
		String typeStr = GetStringFromList(this.types);
		
		return "\n"+title + " " + year + "\nAvg.Rating: " + this.avg_rating
				+ "\nNo. ratings: " + no_ratings + "\nComplexity: " + complexity
				+ "\nPlayers: " + this.min_players + "-" + this.max_players
				+ "\nPlay Time: " + this.min_time + "-" + this.max_time
				+ "\nCategories: " + categoriesStr + "\nMechanics: " + mechsStr;
				//+ "\nTypes: " + typeStr;
	}
	
	public String getJson() {
		String categ;
		
		categ = "[";
		for(int i = 0; i < categories.size(); i++) {
			categ += "\"" + categories.get(i) + "\"";
			if(i != categories.size() - 1) {
				categ += ", ";
			}
		}
		categ += "]";

		String encodedURL;
		try {
			encodedURL = URLEncoder.encode(image_link, "UTF-8");
		}
		catch (Exception e) {
			encodedURL = "";
		}
		
		return "{\n" +
					"\t\"id\": \"" + id + "\",\n" +
					"\t\"title\": \"" + title + "\",\n" +
					"\t\"year\": \"" + year + "\",\n" +
					"\t\"avg_rating\": \"" + avg_rating + "\",\n" +
					"\t\"no_ratings\": \"" + no_ratings + "\",\n" +
					"\t\"complexity\": \"" + complexity + "\",\n" +
					"\t\"min_players\": \"" + min_players + "\",\n" +
					"\t\"max_players\": \"" + max_players + "\",\n" +
					"\t\"min_time\": \"" + min_time + "\",\n" +
					"\t\"max_time\": \"" + max_time + "\",\n" +
					//"\t\"description\":" + description + "\',\n" +
					"\t\"image_link\": \"" + encodedURL + "\",\n" +
					"\t\"categories\": " + categ + "\n" +

				"}";
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
