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
		
		this.phi += Math.toRadians(random.nextInt(90)) * Maths.POM();
		
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
				this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
			}
		}
		
		// Chance to add an enemy
		if(random.nextDouble() < WORLD_VIEW_MUTATION_RATE)
			this.worldViewRequirement[random.nextInt(this.worldViewRequirement.length)][random.nextInt(this.worldViewRequirement[0].length)] = WorldStates.ENEMY.getCode();
		
		if(random.nextDouble() < WORLD_VIEW_MUTATION_RATE)
			this.worldViewRequirement[random.nextInt(this.worldViewRequirement.length)][random.nextInt(this.worldViewRequirement[0].length)] = WorldStates.FLAG.getCode();
		
		this.attacking = AttackRule.getRule(random.nextInt(2));
	}
	
	private boolean hasEnemyTile() {
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
				if(this.worldViewRequirement[i][j] == WorldStates.ENEMY.getCode())
					return true;
			}
		}
		return false;
	}
	
	private boolean hasFlagTile() {
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
				if(this.worldViewRequirement[i][j] == WorldStates.FLAG.getCode())
					return true;
			}
		}
		return false;
	}
	
	
	public void mutate(Random random) {
		if(random.nextDouble() < MOVEMENT_MUTATION) {
			this.movement = MovementRule.getRule(random.nextInt(3) - 1);
		}
		if(random.nextDouble() < ROTATION_MUTATION) {
			this.rotation = RotationRule.getRule(random.nextInt(3) - 1);
		}
		if(random.nextDouble() < ATTACKING_MUTATION) {
			this.attacking = AttackRule.getRule(random.nextInt(2));
		}
		if(random.nextDouble() < ROTATION_AMOUNT_MUTATION) {
			this.phi += Math.toRadians(random.nextInt(ROTATION_AMOUNT_MUTATION_QUANTITY)) * Maths.POM();
		}
		
		boolean hasFlagTile = hasFlagTile();
		boolean hasEnemyTile = hasEnemyTile();
		
		for(int i = 0; i < this.worldViewRequirement.length; i++) {
			for(int j = 0; j < this.worldViewRequirement.length; j++) {
				if(Math.random() < WORLD_VIEW_MUTATION_RATE) {
					WorldStates currentState = WorldStates.getState(this.worldViewRequirement[i][j]);
					
					if(currentState == WorldStates.EMPTY)
						this.worldViewRequirement[i][j] = WorldStates.OCCUPIED.getCode();
					else if(currentState == WorldStates.OCCUPIED)
						this.worldViewRequirement[i][j] = WorldStates.EMPTY.getCode();
					else if(currentState == WorldStates.FLAG) {
						// Move flag to an adjacent square
						
						// Starting west and moving clockwise
						int[] options = new int[]{1,2,3,4};
						
						if(i == 0 || this.worldViewRequirement[i - 1][j] == WorldStates.ENEMY.getCode())
							options[0] = 0;
						if(j == 0 || this.worldViewRequirement[i][j - 1] == WorldStates.ENEMY.getCode())
							options[1] = 0;
						if(i + 1 == this.worldViewRequirement.length || this.worldViewRequirement[i + 1][j] == WorldStates.ENEMY.getCode())
							options[2] = 0;
						if(j + 1 == this.worldViewRequirement.length || this.worldViewRequirement[i][j + 1] == WorldStates.ENEMY.getCode())
							options[3] = 0;
						
						// If can't move mutation then leave it
						if(options[0] == 0 && options[1] == 0 && options[2] == 0 && options[3] == 0)
							return;
						
						int index = 0;
						do {
							index =  random.nextInt(options.length);
						}while(options[index] == 0);
						
						if(index == 0) {
							this.worldViewRequirement[i - 1][j] = WorldStates.FLAG.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else if(index == 1) {
							this.worldViewRequirement[i][j - 1] = WorldStates.FLAG.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else if(index == 2) {
							this.worldViewRequirement[i + 1][j] = WorldStates.FLAG.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else {
							this.worldViewRequirement[i][j + 1] = WorldStates.FLAG.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						
						// Don't keep mutating
						return;
					}
					else if(currentState == WorldStates.ENEMY) {
						// Starting west and moving clockwise
						int[] options = new int[]{1,2,3,4};
						
						if(i == 0 || this.worldViewRequirement[i - 1][j] == WorldStates.FLAG.getCode())
							options[0] = 0;
						if(j == 0 || this.worldViewRequirement[i][j - 1] == WorldStates.FLAG.getCode())
							options[1] = 0;
						if(i + 1 == this.worldViewRequirement.length || this.worldViewRequirement[i + 1][j] == WorldStates.FLAG.getCode())
							options[2] = 0;
						if(j + 1 == this.worldViewRequirement.length || this.worldViewRequirement[i][j + 1] == WorldStates.FLAG.getCode())
							options[3] = 0;
						
						// If can't move mutation then leave it
						if(options[0] == 0 && options[1] == 0 && options[2] == 0 && options[3] == 0)
							return;
						
						int index = 0;
						do {
							index =  random.nextInt(options.length);
						}while(options[index] == 0);
						
						if(index == 0) {
							this.worldViewRequirement[i - 1][j] = WorldStates.ENEMY.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else if(index == 1) {
							this.worldViewRequirement[i][j - 1] = WorldStates.ENEMY.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else if(index == 2) {
							this.worldViewRequirement[i + 1][j] = WorldStates.ENEMY.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						else {
							this.worldViewRequirement[i][j + 1] = WorldStates.ENEMY.getCode();
							this.worldViewRequirement[i][j] = WorldStates.random(random).getCode();
						}
						
						// Don't keep mutating
						return;
					}
				}
				// Chance to create a flag or enemy if one isn't present
				else if(Math.random() < WORLD_VIEW_MUTATION_RATE) {
					if(!hasEnemyTile)
						this.worldViewRequirement[i][j] = WorldStates.ENEMY.getCode();
					else if(!hasFlagTile)
						this.worldViewRequirement[i][j] = WorldStates.FLAG.getCode();
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
