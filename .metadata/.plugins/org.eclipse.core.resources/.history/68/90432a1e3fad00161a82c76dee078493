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
	
	public String toString() {
		return overallScore + game.toString();
	}

}
