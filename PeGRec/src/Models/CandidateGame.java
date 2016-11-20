package Models;

public class CandidateGame {
	
	public Game game;
	public float popularityScore=0;
	public float complexityScore=0;
	public float playersScore=0;
	public float playTimeScore=0;
	public float categoryScore=0;
	public float mechanicScore=0;
	public float typeScore=0;
	public float overallScore=0;
	
	public CandidateGame() {
		
	}
	
	public void CalculateBasicOverallScore() {
		
		overallScore = popularityScore + complexityScore + playersScore + playTimeScore + categoryScore + mechanicScore + typeScore;
	}
	
	private String getStrRange(int min, int max) {
		StringBuilder str = new StringBuilder();
		if (min == max) {
			str.append(Integer.toString(min));
		}
		else {
			str.append(Integer.toString(min));
			str.append("-");
			str.append(Integer.toString(max));
		}
		return str.toString();
	}
	public String GetHtml(int rank) {
		
		String html = ""
		  		+ "<table cellpadding='15' align=\"center\">"
		  		+ "<tr bgcolor=\"white\" height= '200px' style=\"outline: thin solid lightgrey\">"
		  		// First column
		  		+ "<td align=\"center\" width='30px'><h3>"
		  		+  rank + "</h3></br>"
		  		+ "</td>"
		  		// Sec column
		  		+ "<td width='350px' >"
		  		+ "<img class='displayed' src=' "+ game.image_link + "' height:90>"
		  		//+ "<a href=\"\"><img ng-src={{"+game.image_link+"}} alt=\"icon\" width=\"80\" height=\"auto\" class='.img-thumbnail' /></a>"
		  		+ "</td>"
		  		
		  		// Third column
		  		+ "<td width ='550px' >"
		  		+ " <h3><a href= 'https://boardgamegeek.com/boardgame/" +game.id+ "/'>"+game.title+"</a></h3> <small><b>"
		  		+ game.GetShortDescription(200) //+ "</br></br>Avg Rating: " + game.avg_rating + "</br>No. Ratings: " + game.no_ratings 
		  		+ "</b></br><br>Complexity: " + game.complexity + "</br>Players: " + getStrRange(game.min_players, game.max_players) 
		  		+ "</br> Approx Time: " + getStrRange(game.min_time, game.max_time)
		  		+ "</br> Categories: " + game.GetStringFromList(game.categories)
		  		+ "</br> Mechanics: " + game.GetStringFromList(game.mechanics)
		  		+ "</br> Type: " + game.GetStringFromList(game.types)
		  		+ " </small></br></td> </tr></table>";
		  return html;
	}
	
	public String toString() {
		return overallScore + game.toString();
	}

}
