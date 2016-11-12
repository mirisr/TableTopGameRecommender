package Computation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Models.COF;
import Models.CandidateGame;
import Models.Game;
import Models.Tuple;

public class FeatureScores {
	
	public List<CandidateGame>candidateGames;
	private float[][] cofs = new float[8][8];
	private List<String> categoryNames = new ArrayList<String>();
	
	public FeatureScores(List<CandidateGame> candidateGames, List<COF>rawCOFs) {
		this.candidateGames = candidateGames;
		LoadCategoryNames();
		LoadCOFs(rawCOFs);
	}
	
	/*
	 * Calculated individual of the scores
	 * 
	 */
	private void CalculatePopularity(CandidateGame candidateGame, Tuple<Float,Float>minmax) {
		
		Game candidate = candidateGame.game;
		if(candidate.avg_rating <= 4.9) {
			candidateGame.popularityScore = -1 * candidate.no_ratings + candidate.avg_rating;
		}else {
			candidateGame.popularityScore = candidate.no_ratings + candidate.avg_rating;
		}
		
		if (candidateGame.popularityScore < minmax.getLeft())
			minmax.setLeft(candidateGame.popularityScore);
		if (candidateGame.popularityScore > minmax.getRight())
			minmax.setRight(candidateGame.popularityScore);
		
	}
	
	private void CalculateComplexity(Game designatedGame, CandidateGame candidateGame, Tuple<Float,Float>minmax) {
		candidateGame.complexityScore = -1 * 
				Math.abs(designatedGame.complexity - candidateGame.game.complexity);
		
		if (candidateGame.complexityScore < minmax.getLeft())
			minmax.setLeft(candidateGame.complexityScore);
		if (candidateGame.complexityScore > minmax.getRight())
			minmax.setRight(candidateGame.complexityScore);
	}
	
	private void CalculatePlayTime(Game designatedGame, CandidateGame candidateGame, Tuple<Float,Float>minmax) {
		Game candidate = candidateGame.game;
		float avgTimeDG = (designatedGame.min_time + designatedGame.max_time) / 2.0f;
		float avgTimeCG = (candidate.min_time + candidate.max_time) / 2.0f;
		
		float diff = Math.abs(avgTimeDG - avgTimeCG);
		
		if (diff > 5)
			candidateGame.playTimeScore = -1 * diff;
		else
			candidateGame.playTimeScore = avgTimeDG;
		
		if (candidateGame.playTimeScore < minmax.getLeft())
			minmax.setLeft(candidateGame.playTimeScore);
		if (candidateGame.playTimeScore > minmax.getRight())
			minmax.setRight(candidateGame.playTimeScore);
		
	}
	
	private void CalculatePlayers(Game designatedGame, CandidateGame candidateGame, Tuple<Float,Float>minmax) {
		Game candidate = candidateGame.game;
		int sumOfPlayersDG = 0;
		int sumOfPlayersCG = 0;
		int diff = designatedGame.max_players - designatedGame.min_players;
		
		for(int i = designatedGame.min_players; i <= designatedGame.max_players; i++) {
			sumOfPlayersDG += i;
		}
		
		for(int i = candidate.min_players; i <= candidate.max_players; i++) {
			sumOfPlayersCG += -1*i;
		}
		
		candidateGame.playersScore = sumOfPlayersDG + sumOfPlayersCG; 
		
		if(diff>0){
			candidateGame.playersScore *= -1;
		}
		
		if (candidateGame.playersScore < minmax.getLeft())
			minmax.setLeft(candidateGame.playersScore);
		if (candidateGame.playersScore > minmax.getRight())
			minmax.setRight(candidateGame.playersScore);
	}
	
	private void CalculateCategories(List<Game>profile, CandidateGame candidateGame, Tuple<Float,Float>minmax) {
		candidateGame.categoryScore = 0;
		int categoriesInCandidateGame = candidateGame.game.categories.size();
		//For every profile Game
		for(Iterator<Game> i = profile.iterator(); i.hasNext(); ) {
		    Game profileGame = i.next();
		    int categoriesInProfileGame = profileGame.categories.size();
		    
		    //Get every set of categories between the profile game and the candidateGame
		    Set<Tuple<String,String>>Z = new HashSet<Tuple<String,String>>();
		    for(Iterator<String> p = profileGame.categories.iterator(); p.hasNext(); ) {
			    String profileCategory= p.next();
			    for(Iterator<String> c = candidateGame.game.categories.iterator(); c.hasNext(); ) {
				    String candidateCategory= c.next();
				    Tuple<String,String> tuple = new Tuple<String,String>(profileCategory, candidateCategory);
				    Z.add(tuple);
			    }
		    }
		    float COFSums = 0;
		    
		    //Find SUM of the COFS for each set in Z
		    for(Iterator<Tuple<String,String>> t = Z.iterator(); t.hasNext(); ) {
			    Tuple tuple= t.next();
			    int row = categoryNames.indexOf(tuple.getLeft());
			    int col = categoryNames.indexOf(tuple.getRight());
			    COFSums += cofs[row][col];
		    }
		    
		    float valueFromProfileGame = COFSums / (categoriesInCandidateGame * categoriesInProfileGame);
		    candidateGame.categoryScore += valueFromProfileGame;
		    
		    if (candidateGame.categoryScore < minmax.getLeft())
				minmax.setLeft(candidateGame.categoryScore);
			if (candidateGame.categoryScore > minmax.getRight())
				minmax.setRight(candidateGame.categoryScore);
		    
		}
	}
	/*
	 * Calculated all of the scores
	 * 
	 */
	public void CalculateScoresAgainstDesignatedGame(Game designatedGame) {
		Tuple<Float,Float>popMinMax = new Tuple<Float,Float>(Float.MAX_VALUE,Float.MIN_VALUE);
		Tuple<Float,Float>compMinMax = new Tuple<Float,Float>(Float.MAX_VALUE,Float.MIN_VALUE);
		Tuple<Float,Float>playersMinMax = new Tuple<Float,Float>(Float.MAX_VALUE,Float.MIN_VALUE);
		Tuple<Float,Float>timeMinMax = new Tuple<Float,Float>(Float.MAX_VALUE,Float.MIN_VALUE);
		
		for(Iterator<CandidateGame> i = this.candidateGames.iterator(); i.hasNext(); ) {
		    CandidateGame game = i.next();
		    CalculatePopularity(game, popMinMax);
		    CalculateComplexity(designatedGame, game, compMinMax);
		    CalculatePlayTime(designatedGame, game, timeMinMax);
		    CalculatePlayers(designatedGame, game, playersMinMax);
		}
		
		//Normalize scores
		for(Iterator<CandidateGame> i = this.candidateGames.iterator(); i.hasNext(); ) {
		    CandidateGame game = i.next();
		    game.popularityScore = (game.popularityScore - popMinMax.getLeft()) / (popMinMax.getRight() - popMinMax.getLeft());
		    game.complexityScore = (game.complexityScore - compMinMax.getLeft()) / (compMinMax.getRight() - compMinMax.getLeft());
		    game.playTimeScore = (game.playTimeScore - timeMinMax.getLeft()) / (timeMinMax.getRight() - timeMinMax.getLeft());
		    game.playersScore = (game.playersScore - playersMinMax.getLeft()) / (playersMinMax.getRight() - playersMinMax.getLeft());;
		}
	}
	
	
	public void CalculateScoresAgainstProfileGames(List<Game>profile) {
		Tuple<Float,Float>catMinMax = new Tuple<Float,Float>(Float.MAX_VALUE,Float.MIN_VALUE);
		
		for(Iterator<CandidateGame> i = this.candidateGames.iterator(); i.hasNext(); ) {
		    CandidateGame game = i.next();
		    CalculateCategories(profile, game, catMinMax);
		}
		
		//Normalize scores
		for(Iterator<CandidateGame> i = this.candidateGames.iterator(); i.hasNext(); ) {
		    CandidateGame game = i.next();
		    game.categoryScore = (game.categoryScore - catMinMax.getLeft()) / (catMinMax.getRight() - catMinMax.getLeft()); 
		    if(Float.isNaN(game.categoryScore))
		    	game.categoryScore = 0;
		    game.CalculateBasicOverallScore();
		}
	}
	
	private void LoadCOFs(List<COF>rawCOFs) {
		for(Iterator<COF> i = rawCOFs.iterator(); i.hasNext(); ) {
		    COF cof = i.next();
		    int row = categoryNames.indexOf(cof.category1);
		    int col = categoryNames.indexOf(cof.category2);
		    cofs[row][col] = cof.cof;
		}
	}
	
	private void LoadCategoryNames() {
		categoryNames.add("Strategy");
		categoryNames.add("Family");
		categoryNames.add("Thematic");
		categoryNames.add("Customizable");
		categoryNames.add("Wargames");
		categoryNames.add("Abstract");
		categoryNames.add("Party");
		categoryNames.add("Children's");
	}
}
