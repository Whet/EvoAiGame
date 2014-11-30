package ai;

import java.util.Random;

import utils.Maths;

import gameworld.WorldMap;
import gameworld.WorldStates;

public class Rule {
	
	private static final double MOVEMENT_MUTATION = 0.1;
	private static final double ROTATION_MUTATION = 0.1;
	private static final double ATTACKING_MUTATION = 0.1;
	private static final double ROTATION_AMOUNT_MUTATION = 0.1;
	private static final int ROTATION_AMOUNT_MUTATION_QUANTITY = 5;
	private static final double WORLD_VIEW_MUTATION_RATE = 0.1;
	
	private int[][] worldViewRequirement;
	
	private MovementRule movement;
	private RotationRule rotation;
	private AttackRule attacking;
	double phi;
	
	public Rule() {
		this.worldViewRequirement = new int[WorldMap.VIEW_SIZE][WorldMap.VIEW_SIZE];
		
		for (int i = 0; i < worldViewRequirement.length; i++) {
			for (int j = 0; j < worldViewRequirement[i].length; j++) {
				worldViewRequirement[i][j] = WorldStates.EMPTY.getCode();
			}
		}
		
		this.movement = MovementRule.STAND_STILL;
		this.rotation = RotationRule.NO_TURN;
		this.attacking = AttackRule.NO_ATTACK;
		this.phi = 0;
	}
	
	public void initialMutation(Random random) {
		
		this.movement = MovementRule.getRule(random.nextInt(3) - 1);
		this.rotation = RotationRule.getRule(random.nextInt(3) - 1);
		
		this.attacking = AttackRule.getRule(random.nextInt(2));
		
		this.phi += Math.toRadians(random.nextInt(90)) * Maths.POM();
		
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
//				if(Math.random() < WORLD_VIEW_MUTATION_RATE) {
					this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
//				}
			}
		}
	}
	
	public void mutate(Random random) {
		if(random.nextDouble() < MOVEMENT_MUTATION) {
			this.movement = MovementRule.getRule(random.nextInt(3) - 1);
		}
		if(random.nextDouble() < ROTATION_MUTATION) {
			this.rotation = RotationRule.getRule(random.nextInt(3) - 1);
		}
		if(random.nextDouble() < ATTACKING_MUTATION) {
			this.attacking = AttackRule.getRule(2);
		}
		if(random.nextDouble() < ROTATION_AMOUNT_MUTATION) {
			this.phi += Math.toRadians(random.nextInt(ROTATION_AMOUNT_MUTATION_QUANTITY)) * Maths.POM();
		}
		
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
				if(Math.random() < WORLD_VIEW_MUTATION_RATE) {
					this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
				}
			}
		}
	}
	
	public double difference(int[][] otherWorld) {
		
		double dist = 0;
		
		for(int i = 0; i < otherWorld.length; i++) {
			for(int j = 0; j < otherWorld[i].length; j++) {
				dist += Math.abs(worldViewRequirement[i][j] - otherWorld[i][j]);
			}
		}
		
		return dist;
	}

	public void apply(Ai ai) {
		ai.rotate(rotation, phi);
		ai.moveDirection(movement);
		ai.attack(attacking);
	}
	
	public Rule copy() {
		
		Rule rule = new Rule();
		
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement[i].length; j++) {
				rule.worldViewRequirement[i][j] = this.worldViewRequirement[i][j];
			}
		}
		
		rule.movement = this.movement;
		rule.attacking = this.attacking;
		rule.rotation = this.rotation;
		rule.phi = this.phi;
		
		return rule; 
	}

}
