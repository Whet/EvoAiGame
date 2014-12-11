package ai;

import gameworld.WorldMap;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import utils.Maths;

public class Ai {

	public static final int ATTACK_FOV = 90;
	public static final double ATTACK_RANGE = 10;
	
	private static final int ATTACK_RECHARGE = 3;
	public static final int MAX_HEALTH = 100;
	
	private static final int DAMAGE = 25;
	
	private Set<Rule> rules;
	public Map<Rule, Integer> ruleUseCount;
	
	private double x, y;
	private double rotation;
	private int move;
	private int health;
	
	private boolean isAttacking;
	private boolean hasFlag;
	
	public WorldMap map;
	private int recharge;
	
	private Point spawn;
	private double spawnAngle;
	
	public Ai(WorldMap map) {
		
		this.map = map;
		
		this.x = 0;
		this.y = 0;
		this.rotation = 0;
		this.move = 0;
		this.recharge = 0;
		
		this.rules = new HashSet<>();
		this.ruleUseCount = new HashMap<>();
		
		this.isAttacking = false;
		this.hasFlag = false;
	}
	
	public boolean hasFlag() {
		return hasFlag;
	}

	public void setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}

	public Set<Rule> getRules() {
		return rules;
	}

	public void setRules(Set<Rule> rules) {
		this.rules = rules;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getRotation() {
		return this.rotation;
	}
	
	public void applyRules(int[][] gridView) {
		
		this.isAttacking = false;
		
		Rule closestRule = getClosestRule(gridView);
		closestRule.apply(this);
		
		if(!this.ruleUseCount.containsKey(closestRule))
			this.ruleUseCount.put(closestRule, 0);
		
		this.ruleUseCount.put(closestRule, this.ruleUseCount.get(closestRule) + 1);
		
		act();
	}
	
	public void act() {
		
		if(this.recharge > 0)
			this.recharge--;
		
		move();
	}

	private void move() {
		
		double x, y;
		
		x = this.x + Math.cos(rotation) * move;
		y = this.y + Math.sin(rotation) * move;
		
		if(map.isValid(x, y)) {
			this.x = x;
			this.y = y;
		}
		
	}

	private Rule getClosestRule(int[][] gridView) {
		
		int[][] smoothedWorld = gaussianSmooth(gridView);
		
		Rule closestRule = null;
		double closestRuleDist = 0;
		
		for(Rule rule:this.rules) {
			double dist = rule.difference(smoothedWorld);
			if(closestRule == null || dist < closestRuleDist) {
				closestRule = rule;
				closestRuleDist = dist;
			}
		}
		
		return closestRule;
	}

	private int[][] gaussianSmooth(int[][] gridView) {
		return gridView;
	}

	public void rotate(RotationRule r, double phi) {
		switch(r) {
			case NO_TURN:
				this.rotation += r.getCode() * phi;
			break;
			case TURN_LEFT:
				this.rotation += r.getCode() * phi;
			break;
			case TURN_RIGHT:
				this.rotation -= r.getCode() * phi;
			break;
		}
	}

	public void moveDirection(MovementRule f) {
		switch(f) {
			case MOVE_BACKWARD:
				this.move = f.getCode();
			break;
			case MOVE_FORWARD:
				this.move = f.getCode();
			break;
			case STAND_STILL:
				this.move = f.getCode();
			break;
		}
	}

	public void attack(AttackRule a) {
		if(a == AttackRule.ATTACK)
			this.isAttacking = true;
	}
	
	public static Set<Rule> randomRules(Random random) {
		
		Set<Rule> rules = new HashSet<>();
		
		for(int i = 0; i < 100; i++) {
			Rule rule = new Rule();
			
			rule.initialMutation(random);
			
			rules.add(rule);
		}
		
		return rules;
	}

	public boolean isAttacking() {
		return this.isAttacking;
	}

	public void setRotation(double phi) {
		this.rotation = phi;
	}
	
	public boolean isOnTarget(Ai target) {
		
		boolean b = this.recharge == 0 && isAttacking() && Maths.getDistance(getX(), getY(), target.getX(), target.getY()) < ATTACK_RANGE && 
					(Maths.angleDifference(Maths.getDegrees(this.getX(), this.getY(), target.getX(), target.getY()), Math.toDegrees(this.rotation)) <= ATTACK_FOV * 0.5);
		
		return b;
	}

	public boolean attack(Ai target) {
		
		boolean couldAttack = this.isAttacking() && this.recharge == 0;
		
		if(couldAttack)
			this.recharge = ATTACK_RECHARGE;
		
		return couldAttack && isOnTarget(target);
	}

	public int getRecharge() {
		return recharge;
	}

	public void setRecharge(int recharge) {
		this.recharge = recharge;
	}
	
	public void hit(int damage) {
		this.health -= damage;
		
		if(this.isDead()) {
			this.respawn();
		}
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}

	public int getDamage() {
		return DAMAGE;
	}
	
	public int getHealth() {
		return this.health;
	}

	public void setSpawn(Point spawn, double angle) {
		this.spawn = spawn;
		this.spawnAngle = angle;
	}

	public void respawn() {
		this.setX(spawn.x);
		this.setY(spawn.y);
		this.setRotation(spawnAngle);
		this.setHasFlag(false);
		this.recharge = 0;
		this.health = MAX_HEALTH;
	}
	
}
