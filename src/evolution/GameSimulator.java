package evolution;

import java.awt.Point;

import utils.Maths;
import ai.Ai;
import evolution.Evolver.Individual;
import gameworld.WorldMap;

public class GameSimulator {

	// 20 turns a second
	// 1 min of gameplay
	private static final int MAX_GAME_TURNS = 1200;
	// 30 secs of gameplay
//	private static final int MAX_GAME_TURNS = 600;
	
	public static int runGame(WorldMap map, Individual individual1, Individual individual2) {
		
		// Get average score between both starts
//		double individual1Score = 0;
//		double individual2Score = 0;
		
		map.setAi(individual1.getAi(), individual2.getAi());
		
		runSubGame(map, individual1, individual2);
		
//		individual1Score = individual1.getFitness();
//		individual2Score = individual2.getFitness();
//		
//		runSubGame(map, individual2, individual1);
//		
//		individual1.setFitness(individual1Score + individual1.getFitness());
//		individual2.setFitness(individual2Score + individual2.getFitness());
//		
//		individual1.setFitness(individual1.getFitness() / 2.0);
//		individual2.setFitness(individual2.getFitness() / 2.0);
		
		
		// Use different scores for different experiments
		
		// Farthest score counts!
//		if(Math.abs(individual1.getFlagScore() - individual2.getFlagScore()) > Math.abs(individual1.getKillScore() - individual2.getKillScore())) {
//			if(individual1.getFlagScore() > individual2.getFlagScore()) {
//				individual1.subjectiveFitness++;
//				return 1;
//			}
//		}
//		else {
//			if(individual1.getKillScore() > individual2.getKillScore()) {
//				individual1.subjectiveFitness++;
//				return 1;
//			}
//		}
		
		int score1 = 0;
		int score2 = 0;
		
		if(individual1.getFlagScore() > individual2.getFlagScore()) {
			score1++;
		}
		else if(individual1.getFlagScore() < individual2.getFlagScore()) {
			score2++;
		}
		
		if(individual1.getKillScore() > individual2.getKillScore()) {
			score1++;
		}
		else if(individual1.getKillScore() < individual2.getKillScore()) {
			score2++;
		}
		
		individual1.resetScoring();
		individual2.resetScoring();
		
		if(score1 > score2)
			individual1.subjectiveFitness++;
		
		return 0;
	}

	private static void runSubGame(WorldMap map, Individual individual1, Individual individual2) {
		Ai ai1 = individual1.getAi();
		Ai ai2 = individual2.getAi();
		
		Point start1 = map.getStart1();
		ai1.setX(start1.x);
		ai1.setY(start1.y);
		ai1.setRotation(Math.PI / 2);
		ai1.setRecharge(0);
		
		Point start2 = map.getStart2();
		ai2.setX(start2.x);
		ai2.setY(start2.y);
		ai2.setRotation(-Math.PI / 2);
		ai2.setRecharge(0);
		
		int ai1FlagCaptures = 0;
		int ai2FlagCaptures = 0;
		
		ai1.setHasFlag(false);
		ai2.setHasFlag(false);
		
		int ai1Frags = 0;
		int ai2Frags = 0;
		
		int ai1Shots = 0;
		int ai2Shots = 0;
		
		int timer = 0;
		
		for(int i = 0; i < MAX_GAME_TURNS; i++) {
			if(timer == 0) {
				ai1.applyRules(map.getGridView(ai1));
				ai2.applyRules(map.getGridView(ai2));
				timer = 2;
			}
			else {
				ai1.act();
				ai2.act();
				timer--;
			}
			
			// Ai 1 fitness
			double distance = Maths.getDistance(ai1.getX(), ai1.getY(), start1.x, start1.y);
			
			if(distance < 5 && ai1.hasFlag()) {
				ai1.setHasFlag(false);
				ai1FlagCaptures+= 2;
			}
			
			distance = Maths.getDistance(ai1.getX(), ai1.getY(), start2.x, start2.y);
			
			if(distance < 5 && !ai1.hasFlag()) {
				ai1.setHasFlag(true);
				ai1FlagCaptures++;
			}
			
			if(ai1.isAttacking() && ai1.getRecharge() == 0)
				ai1Shots++;
			
			if(ai1.successfulAttack(ai2)) {
				ai1Frags++;
				ai2.setX(start2.x);
				ai2.setY(start2.y);
				ai2.setHasFlag(false);
			}

			// Ai 2 fitness
			distance = Maths.getDistance(ai2.getX(), ai2.getY(), start2.x, start2.y);
			
			if(distance < 5 && ai2.hasFlag()) {
				ai2.setHasFlag(false);
				ai2FlagCaptures+=2;
			}
			
			distance = Maths.getDistance(ai2.getX(), ai2.getY(), start1.x, start1.y);
			
			if(distance < 5 && !ai2.hasFlag()) {
				ai2.setHasFlag(true);
				ai2FlagCaptures++;
			}
			
			if(ai2.isAttacking() && ai2.getRecharge() == 0)
				ai2Shots++;
			
			if(ai2.successfulAttack(ai1)) {
				ai2Frags++;
				ai1.setX(start1.x);
				ai1.setY(start1.y);
				ai1.setHasFlag(false);
			}
			
		}
		
		individual1.addFlagScore(ai1FlagCaptures);
		individual1.addFrags(ai1Frags);
		individual1.addShots(ai1Shots);
		
		individual2.addFlagScore(ai2FlagCaptures);
		individual2.addFrags(ai2Frags);
		individual2.addShots(ai2Shots);
	}

}
