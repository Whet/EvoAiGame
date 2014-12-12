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
	
	public static int runGame(WorldMap map, Individual individual1, Individual individual2) {
		
		map.setAi(individual1.getAi(), individual2.getAi());
		
		int subGameOutcome = runSubGame(map, individual1, individual2);

		individual1.resetScoring();
		individual2.resetScoring();
		
		individual1.subjectiveFitness += subGameOutcome;
		
		return subGameOutcome;
	}

	private static int runSubGame(WorldMap map, Individual individual1, Individual individual2) {
		Ai ai1 = individual1.getAi();
		Ai ai2 = individual2.getAi();
		
		Point start1 = map.getStart1();
		ai1.setSpawn(start1, Math.PI / 2);
		ai1.respawn();
		
		Point start2 = map.getStart2();
		ai2.setSpawn(start2, -Math.PI / 2);
		ai2.respawn();
		
		int ai1FlagCaptures = 0;
		int ai2FlagCaptures = 0;
		
		ai1.setHasFlag(false);
		ai2.setHasFlag(false);
		
		int ai1DamageGiven = 0;
		int ai2DamageGiven = 0;
		
		int ai1DamageTaken = 0;
		int ai2DamageTaken = 0;
		
		int ai1InaccuracyPenalty = 0;
		int ai2InaccuracyPenalty = 0;
		
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
			
			if(!ai1.isOnTarget(ai2) && ai1.isAttacking() && ai1.getRecharge() == 0)
				ai1InaccuracyPenalty++;
			
			if(ai1.attack(ai2)) {
				ai1DamageGiven += ai1.getDamage();
				ai2DamageTaken += ai1.getDamage();
				ai2.hit(ai1.getDamage());
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
			
			if(!ai2.isOnTarget(ai1) && ai2.isAttacking() && ai2.getRecharge() == 0)
				ai2InaccuracyPenalty++;
			
			if(ai2.attack(ai1)) {
				ai2DamageGiven += ai2.getDamage();
				ai1DamageTaken += ai2.getDamage();
				ai1.hit(ai2.getDamage());
			}
			
		}
		
		individual1.addFlagScore(ai1FlagCaptures);
		int ai1DamageScore = ai1DamageGiven - ai1DamageTaken/2 - ai1InaccuracyPenalty;
		individual1.addCombatScore(ai1DamageScore);
		individual1.addShots(ai1InaccuracyPenalty);
		
		individual2.addFlagScore(ai2FlagCaptures);
		int ai2DamageScore = ai2DamageGiven - ai2DamageTaken/2 - ai2InaccuracyPenalty;
		individual2.addCombatScore(ai2DamageScore);
		individual2.addShots(ai2InaccuracyPenalty);
		
		return scoreCalculation(ai1FlagCaptures, ai1DamageScore, ai2FlagCaptures, ai2DamageScore);
	}

	private static int scoreCalculation(int ai1FlagCaptures, int ai1DamageScore, int ai2FlagCaptures, int ai2DamageScore) {
		
		int score1 = 0;
		int score2 = 0;
		
		if(ai1FlagCaptures > ai2FlagCaptures) {
			score1++;
		}
		else if(ai1FlagCaptures < ai2FlagCaptures) {
			score2++;
		}
		
		if(ai1DamageScore > ai2DamageScore) {
			score1++;
		}
		else if(ai1DamageScore < ai2DamageScore) {
			score2++;
		}
		
		if(score1 > score2)
			return 1;
		
		return 0;
	}

}
