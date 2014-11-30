package evolution;

import java.awt.Point;

import utils.Maths;
import ai.Ai;
import evolution.Evolver.Individual;
import gameworld.WorldMap;

public class GameSimulator {

	// 5 mins of gameplay
//	private static final int MAX_GAME_TURNS = 3000;
	// 1 min of gameplay
	private static final int MAX_GAME_TURNS = 600;
	
	private static final double CAPTURE_SCORE = 4000;
	private static final double PICKUP_SCORE = 500;
	
	private static final double ATTACK_SCORE = 2000;
	private static final double ATTACK_PENALTY = 200;
	private static final double MISS_PENALTY = 100;
	
	public static void runGame(WorldMap map, Individual individual1, Individual individual2) {
		
		// Get average score between both starts
		double individual1Score = 0;
		double individual2Score = 0;
		
		map.setAi(individual1.getAi(), individual2.getAi());
		
		runSubGame(map, individual1, individual2);
		
		individual1Score = individual1.getFitness();
		individual2Score = individual2.getFitness();
		
		runSubGame(map, individual2, individual1);
		
		individual1.setFitness(individual1Score + individual1.getFitness());
		individual2.setFitness(individual2Score + individual2.getFitness());
		
		individual1.setFitness(individual1.getFitness() / 2.0);
		individual2.setFitness(individual2.getFitness() / 2.0);
		
		if(individual1.getFitness() > individual2.getFitness())
			individual1.subjectiveFitness++;
	}

	private static void runSubGame(WorldMap map, Individual individual1, Individual individual2) {
		Ai ai1 = individual1.getAi();
		Ai ai2 = individual2.getAi();
		
		Point start1 = map.getStart1();
		ai1.setX(start1.x);
		ai1.setY(start1.y);
		ai1.setRotation(0);
		
		Point start2 = map.getStart2();
		ai2.setX(start2.x);
		ai2.setY(start2.y);
		ai2.setRotation(Math.PI);
		
		double ai1MaxDist = 0;
		double ai2MaxDist = 0;
		
		double ai1DistanceToFlag = 99999;
		double ai2DistanceToFlag = 99999;
		
		double ai1FlagScore = 0;
		double ai2FlagScore = 0;
		
		int ai1FlagCaptures = 0;
		int ai2FlagCaptures = 0;
		
		ai1.setHasFlag(false);
		ai2.setHasFlag(false);
		
		double ai1TurnsToGetFlag = MAX_GAME_TURNS;
		double ai2TurnsToGetFlag = MAX_GAME_TURNS;
		
		double ai1TurnsToCapFlag = MAX_GAME_TURNS;
		double ai2TurnsToCapFlag = MAX_GAME_TURNS;
		
		double ai1CombatScore = 0;
		double ai2CombatScore = 0;
		
		int ai1Frags = 0;
		int ai2Frags = 0;
		
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
			if(distance > ai1MaxDist)
				ai1MaxDist = distance;
			
			if(distance < 5 && ai1.hasFlag()) {
				ai1.setHasFlag(false);
				ai1FlagScore += CAPTURE_SCORE;

				if(ai1TurnsToCapFlag > i) {
					ai1TurnsToCapFlag = i;
				}
				
				ai1FlagCaptures++;
				
			}
			
			distance = Maths.getDistance(ai1.getX(), ai1.getY(), start2.x, start2.y);
			if(distance < ai1DistanceToFlag)
				ai1DistanceToFlag = distance;
			
			if(distance < 5 && !ai1.hasFlag()) {
				ai1.setHasFlag(true);
				ai1FlagScore += PICKUP_SCORE;
				
				if(ai1TurnsToGetFlag > i) {
					ai1TurnsToGetFlag = i;
				}
				
				ai1DistanceToFlag = 0;
			}
			
			if(ai1.successfulAttack(ai2)) {
				ai1CombatScore += ATTACK_SCORE;
				ai2CombatScore -= ATTACK_PENALTY;
				ai1Frags++;
				ai2.setX(start2.x);
				ai2.setY(start2.y);
			}
			else if(ai1.isAttacking() ) {
				ai1CombatScore -= MISS_PENALTY;
			}

			// Ai 2 fitness
			distance = Maths.getDistance(ai2.getX(), ai2.getY(), start2.x, start2.y);
			if(distance > ai2MaxDist)
				ai2MaxDist = distance;
			
			if(distance < 5 && ai2.hasFlag()) {
				ai2.setHasFlag(false);
				ai2FlagScore += CAPTURE_SCORE;
				
				if(ai2TurnsToCapFlag > i) {
					ai2TurnsToCapFlag = i;
				}
				
				ai2FlagCaptures++;
			}
			
			distance = Maths.getDistance(ai2.getX(), ai2.getY(), start1.x, start1.y);
			if(distance < ai2DistanceToFlag)
				ai2DistanceToFlag = distance;
			
			if(distance < 5 && !ai2.hasFlag()) {
				ai2.setHasFlag(true);
				ai2FlagScore += PICKUP_SCORE;
				
				if(ai2TurnsToGetFlag > i) {
					ai2TurnsToGetFlag = i;
				}
				
				ai2DistanceToFlag = 0;
			}
			
			if(ai2.successfulAttack(ai1)) {
				ai2CombatScore += ATTACK_SCORE;
				ai1CombatScore -= ATTACK_PENALTY;
				ai2Frags++;
				ai1.setX(start1.x);
				ai1.setY(start1.y);
			}
			else if(ai2.isAttacking() ) {
				ai2CombatScore -= MISS_PENALTY;
			}
			
		}
		
		double score1 = ai1FlagScore + ai1CombatScore - ai1DistanceToFlag;
		
		if(ai1FlagScore > 0) {
			score1 += (MAX_GAME_TURNS - ai1TurnsToGetFlag);
			if(ai1FlagCaptures > 0)
				score1 += (MAX_GAME_TURNS - ai1TurnsToCapFlag);
		}
		
		double score2 = ai2FlagScore + ai2CombatScore - ai2DistanceToFlag;
		
		if(ai2FlagScore > 0) {
			score2 += (MAX_GAME_TURNS - ai2TurnsToGetFlag);
			if(ai2FlagCaptures > 0)
				score2 += (MAX_GAME_TURNS - ai2TurnsToCapFlag);
		}
		
		individual1.setFitness(score1);
		individual1.setFlagCaps(ai1FlagCaptures);
		individual1.setFrags(ai1Frags);
		
		individual2.setFitness(score2);
		individual2.setFlagCaps(ai2FlagCaptures);
		individual2.setFrags(ai2Frags);
	}

}
