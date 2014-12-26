package evolution;

import gameworld.WorldMap;
import graph.ChampionGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ruleIO.LoadSave;
import GUI.GUI;
import ai.Ai;
import ai.Rule;

public class Evolver {

	private static final int GENERATIONS = 300;
	private static final int POPULATION_SIZE = 50;
	private static final int CARRY_OVER_POPULATION = 20;
	
	private static final int S = 10;
	
	public static void main(String[] args) {
		File saveFolder = new File(args[0]);
		new Evolver(saveFolder);
	}
	
	private PriorityQueue<Individual> population1;
	private PriorityQueue<Individual> population2;
	private WorldMap map;
	private Random random;
//	private Graph graph;
	
	private List<Individual> champions1;
	private List<Individual> champions2;
	
	public Evolver(File saveFolder) {
		
		champions1 = new ArrayList<>();
		champions2 = new ArrayList<>();
		
//		graph = new Graph();
		
		random = new Random();
		
		map = new WorldMap();
		
		runEvolution(saveFolder);
		
		ChampionGraph.showChampionGraph(fightChampions(champions1), fightChampions(champions2));
		
//		saveChampions(saveFolder);
		
		openGUI(GENERATIONS, population1.peek().getAiCopy(), population2.peek().getAiCopy(), new WorldMap());
	}
	
//	private void saveChampions(File saveFolder) {
//		for(int i = 0; i < champions1.size(); i++) {
//			LoadSave.saveAi(new File(saveFolder.getAbsoluteFile() + "/champions1/champion" + i), champions1.get(i).getAiCopy());
//		}
//		for(int i = 0; i < champions2.size(); i++) {
//			LoadSave.saveAi(new File(saveFolder.getAbsoluteFile() + "/champions2/champion" + i), champions2.get(i).getAiCopy());
//		}
//	}

	private List<Integer> fightChampions(List<Individual> champions) {
		
		List<Integer> wins = new ArrayList<>();
		
		for(Individual champion:champions) {
			
			Integer winCount = 0;
			
			for(int i = 0; i < champions.size(); i++) {
				if(champion == champions.get(i))
					continue;
				
				if(GameSimulator.runGame(map, champion, champions1.get(i)) == 1)
					winCount++;
			}
			
			wins.add(winCount);
		}
		
		return wins;
	}

	private void runCompetition(Individual ind1, PriorityQueue<Individual> competitionPop) {
		
		List<Individual> competitors = new ArrayList<>(competitionPop);
		
		for(int i = 0; i < S; i++) {
			GameSimulator.runGame(map, ind1, competitors.get(random.nextInt(competitors.size())));
		}
	}

	private void runEvolution(File saveFolder) {
		
		Individual[] currentPopulation1 = new Individual[POPULATION_SIZE];
		Individual[] currentPopulation2 = new Individual[POPULATION_SIZE];
		
		population1 = new GeneticQueue();
		population2 = new GeneticQueue();
		
		System.out.println("Starting");
		
		// Create populations
		for(int j = population1.size(); j < POPULATION_SIZE; j++) {
			Ai ai1 = new Ai(map);
			ai1.setRules(Ai.randomRules(random));
			Individual individual1 = new Individual(ai1, random);
			population1.add(individual1);
			currentPopulation1[j] = individual1;
			
			Ai ai2 = new Ai(map);
			ai2.setRules(Ai.randomRules(random));
			Individual individual2 = new Individual(ai2, random);
			population2.add(individual2);
			currentPopulation2[j] = individual2;
		}
		
		// FIGHT!!!
		for(int i = 0; i < POPULATION_SIZE; i++) {
			runCompetition(currentPopulation1[i], population2);
			runCompetition(currentPopulation2[i], population1);
		}
		
		System.out.println("Generated Initial Population");
		
		System.out.println("BATTLE REPORT 0");
		System.out.println("POP1: BEST SOLDIER " + population1.peek() + " VICTORIES " + population1.peek().subjectiveFitness + " flag caps " + population1.peek().getAverageFlagScore() + " frags " + population1.peek().getAverageCombatScore());
		System.out.println("POP2: BEST SOLDIER " + population2.peek() + " VICTORIES " + population2.peek().subjectiveFitness + " flag caps " + population2.peek().getAverageFlagScore() + " frags " + population2.peek().getAverageCombatScore());

//		graph.updateData(population1, population2, 0);
		openGUI(0, population1.peek().getAiCopy(), population2.peek().getAiCopy(), new WorldMap());

		champions1.add(population1.peek());
		champions2.add(population2.peek());
		
		// Continue battle for generations to come!!!
		for(int i = 1; i < GENERATIONS; i++) {
			
			Individual[] carryOver1 = new Individual[CARRY_OVER_POPULATION];
			Individual[] carryOver2 = new Individual[CARRY_OVER_POPULATION];
			
			// Carry over previous generation champions
			for(int j = 0; j < CARRY_OVER_POPULATION; j++) {
				carryOver1[j] = population1.poll();
				carryOver2[j] = population2.poll();
			}
			
			// New generation
			population1 = new GeneticQueue();
			population2 = new GeneticQueue();
			
			currentPopulation1 = new Individual[POPULATION_SIZE];
			currentPopulation2 = new Individual[POPULATION_SIZE];
			
			// Add carry over population
//			for(int j = 0; j < CARRY_OVER_POPULATION; j++) {
//				Individual individual1 = new Individual(carryOver1[j].getAiCopy(), random);
//				individual1.name = carryOver1[j].name;
//				currentPopulation1[j] = individual1;
//				population1.add(individual1);
//				
//				Individual individual2 = new Individual(carryOver2[j].getAiCopy(), random);
//				individual2.name = carryOver2[j].name;
//				currentPopulation2[j] = individual2;
//				population2.add(individual2);
//			}
			
			// Add new population by crossover
			for(int j = 0; j < POPULATION_SIZE; j++) {
				Individual individual1 = new Individual(chooseParent(carryOver1), chooseParent(carryOver1), random);
				population1.add(individual1);
				currentPopulation1[j] = individual1;
				
				Individual individual2 = new Individual(chooseParent(carryOver2), chooseParent(carryOver2), random);
				population2.add(individual2);
				currentPopulation2[j] = individual2;
			}
			
			// FIGHT!!!
			for(int k = 0; k < POPULATION_SIZE; k++) {
				runCompetition(currentPopulation1[k], population2);
				runCompetition(currentPopulation2[k], population1);
			}
			
			// Re-add elements so they get sorted
			List<Individual> ind1 = new ArrayList<>();
			List<Individual> ind2 = new ArrayList<>();
			
			while(!population1.isEmpty()) {
				ind1.add(population1.poll());
				ind2.add(population2.poll());
			}
			
			for(int k = 0; k < ind1.size(); k++) {
				population1.add(ind1.get(k));
				population2.add(ind2.get(k));
			}
			
			System.out.println("BATTLE REPORT " + i);
			System.out.println("POP1: BEST SOLDIER " + population1.peek() + " VICTORIES " + population1.peek().subjectiveFitness + " flag caps " + population1.peek().getAverageFlagScore() + " frags " + population1.peek().getAverageCombatScore());
			System.out.println("POP2: BEST SOLDIER " + population2.peek() + " VICTORIES " + population2.peek().subjectiveFitness + " flag caps " + population2.peek().getAverageFlagScore() + " frags " + population2.peek().getAverageCombatScore());
			
			champions1.add(population1.peek());
			champions2.add(population2.peek());
			LoadSave.saveAi(new File(saveFolder.getAbsoluteFile() + "/champions1/champion" + i), population1.peek().getAiCopy());
			LoadSave.saveAi(new File(saveFolder.getAbsoluteFile() + "/champions2/champion" + i), population2.peek().getAiCopy());
			
			int mean1 = 0;
			for(Individual individual:population1) {
				for(Entry<Rule, Integer> ruleEntry:individual.getAi().ruleUseCount.entrySet()) {
					if(ruleEntry.getValue() > 0)
						mean1 ++;
				}
			}
			mean1 /= population1.size();
			int mean2 = 0;
			for(Individual individual:population2) {
				for(Entry<Rule, Integer> ruleEntry:individual.getAi().ruleUseCount.entrySet()) {
					if(ruleEntry.getValue() > 0)
						mean2 ++;
				}
			}
			mean2 /= population2.size();
			
			System.out.println("Mean rules used population1 " + mean1 + "   Mean rules used population2 " + mean2);
			
//			graph.updateData(population1, population2, i);
			
			// Show cool ppl
			if(i >= 200 && i % 50 == 0)
				openGUI(i, population1.peek().getAiCopy(), population2.peek().getAiCopy(), new WorldMap());
		}
	}

	private Individual chooseParent(Individual[] previousPopulation) {

		int totalFitness = 0;
		
		for(int i = 0; i < previousPopulation.length; i++) {
			totalFitness += previousPopulation[i].subjectiveFitness + 1;
		}
		
		Individual[] selectionWheel = new Individual[totalFitness];
		
		int index = 0;
		
		for(int i = 0; i < previousPopulation.length; i++) {
			
			Individual individual = previousPopulation[i];
			for(int j = 0; j < individual.subjectiveFitness + 1; j++) {
				selectionWheel[index + j] = individual;
			}
			
			index += individual.subjectiveFitness + 1;
			
		}
		
		return selectionWheel[random.nextInt(selectionWheel.length)];
		
	}

	private void openGUI(int generation, Ai ai1, Ai ai2, WorldMap map) {
		final GUI frame = new GUI(generation, ai1, ai2, map);
		
		final Timer timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.repaint();
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setSize(500, 600);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.createBuffers();
				timer.start();
			}
		});
	}
	
	public static class Individual {
		
		String name;
		private Ai ai;
		private double fitness;
		public int subjectiveFitness;
		private int flagScore;
		private double combatScore;
		private int recordCombatScore;
		private int recordFlagCaps;
		private int gamesPlayed;
		private int shots;
		private int recordShots;
		private int recordDamageDealt;
		
		private String randomName(Random random) {
			switch(random.nextInt(5)) {
				case 0 :
					return "Dave";
				case 1 :
					return "Barry";
				case 2 :
					return "Fred";
				case 3 :
					return "Alex";
				case 4 :
					return "Charlotte";
				default :
					return "Bertie";
			}
		}
		
		public Individual(Ai ai, Random random) {
			this.name = randomName(random) + random.nextLong();
			this.ai = ai;
			this.fitness = 0;
			this.combatScore = 0;
			this.recordFlagCaps = 0;
			this.recordCombatScore = 0;
			this.recordShots = 0;
			this.gamesPlayed = 0;
			this.shots = 0;
			this.recordDamageDealt = 0;
		}
		
		// Indivdual with crossover
		public Individual(Individual individual, Individual individual2, Random random) {
			
			this.ai = new Ai(individual.ai.map);
			
			int rules = 50;
			
			List<Rule> rules1 = new ArrayList<>(individual.ai.getRules());
			List<Rule> rules2 = new ArrayList<>(individual2.ai.getRules());
			
			Set<Rule> crossoverRules = new HashSet<>();
			
			for(int i = 0; i < rules; i++) {
				if(random.nextBoolean())
					crossoverRules.add(rules1.get(random.nextInt(rules1.size())).copy());
				else
					crossoverRules.add(rules2.get(random.nextInt(rules2.size())).copy());
			}
			
			Set<Rule> copyRules = new HashSet<>();
			for(Rule rule:crossoverRules) {
				copyRules.add(rule.copy());
			}
			
			this.ai.setRules(copyRules);
			
			this.name = randomName(random) + random.nextLong();
			this.fitness = 0;
			this.combatScore = 0;
			this.recordFlagCaps = 0;
			this.recordCombatScore = 0;
			this.recordShots = 0;
			this.gamesPlayed = 0;
			this.shots = 0;
			this.recordDamageDealt = 0;
			
			this.mutate(random);
		}

		public int getFlagScore() {
			return flagScore;
		}

		public void addFlagScore(int flagCaps) {
			this.flagScore += flagCaps;
		}
		
		@Override
		public String toString() {
			return name;
		}

		public Ai getAiCopy() {
			Ai ai = new Ai(this.ai.map);
			
			Set<Rule> childRules = new HashSet<>();
			
			for(Rule rule:this.ai.getRules()) {
				Rule ruleClone = rule.copy();
				childRules.add(ruleClone);
			}
			
			ai.setRules(childRules);
			ai.ruleUseCount = this.ai.ruleUseCount;
			
			return ai;
		}

		public void mutate(Random random) {
			
			Set<Rule> mutantRules = new HashSet<>();
			
			for(Rule rule:this.ai.getRules()) {
				Rule ruleClone = rule.copy();
				ruleClone.mutate(random);
				mutantRules.add(ruleClone);
			}
			
			ai.setRules(mutantRules);
		}

		public Ai getAi() {
			return this.ai;
		}

		public double getFitness() {
			return fitness;
		}

		public void setFitness(double fitness) {
			this.fitness = fitness;
		}

		public void addCombatScore(double combatScore) {
			this.combatScore += combatScore;
		}

		public double getCombatScore() {
			return this.combatScore;
		}

		public void resetScoring() {
			this.recordCombatScore += this.combatScore;
			this.recordFlagCaps += this.flagScore;
			this.recordShots += this.shots;
			
			this.combatScore = 0;
			this.flagScore = 0;
			this.shots = 0;
			this.gamesPlayed ++;
		}
		
		public double getAverageFlagScore() {
			return this.recordFlagCaps / (double)this.gamesPlayed;
		}
		
		public double getAverageCombatScore() {
			
			if(this.recordCombatScore == 0)
				return -0;
			
			return this.recordCombatScore/ (double)this.gamesPlayed;
		}
		
		public double getAverageDamageDealt() {
			
			if(this.recordDamageDealt == 0)
				return -0;
			
			return this.recordDamageDealt/ (double)this.gamesPlayed;
		}
		
		public void addDamageDealt(int damageGiven) {
			this.recordDamageDealt += damageGiven;
		}

		public void addShots(int shots) {
			this.shots += shots;
		}

		public int getShots() {
			return shots;
		}

		public double getKillScore() {
			
			if(this.combatScore == 0)
				return -0;
			
			return this.combatScore;
		}

	}
	
	private static class GeneticQueue extends PriorityQueue<Individual> {
		
		public GeneticQueue() {
			super(POPULATION_SIZE, new Comparator<Individual>() {
				
				@Override
				public int compare(Individual i1, Individual i2) {
					
					if(i1.subjectiveFitness > i2.subjectiveFitness)
						return -1;
					else if(i2.subjectiveFitness > i1.subjectiveFitness)
						return 1;
					
					// Then return based on damage
					if(i1.getAverageCombatScore() > i2.getAverageCombatScore())
						return -1;
					else if(i2.getAverageCombatScore() > i1.getAverageCombatScore())
						return 1;
					
					return 0;
				}
				
			});
		}
		
	}
	
}
