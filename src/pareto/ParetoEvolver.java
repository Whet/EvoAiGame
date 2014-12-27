package pareto;

import gameworld.WorldMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ruleIO.LoadSave;
import GUI.GUI;
import ai.Ai;
import ai.Rule;

public class ParetoEvolver {

	private static final int GENERATIONS = 100;
	private static final int POPULATION_SIZE = 10;
	private static final int PARETO_FRONT_SIZE = 1;
	
	private static final int S = 9;
	
	public static void main(String[] args) {
		File saveFolder = new File(args[0]);
		new ParetoEvolver(saveFolder);
	}
	
	private List<Individual> population;
	private List<Individual> paretoFront;
	private List<Individual> oldParetoFront;
	
	private WorldMap map;
	private Random random;
	
	public ParetoEvolver(File saveFolder) {
		
		random = new Random();
		map = new WorldMap();
		runEvolution(saveFolder);
	}
	
	private void runCompetition(Individual individual, List<Individual> competitors) {
		
		for(int i = 0; i < S; i++) {
			
			Individual gladiator = null;
			do {
				gladiator = competitors.get(random.nextInt(competitors.size()));
				if(individual.victories.size() >= POPULATION_SIZE - 1)
					return;
			}
			while(individual.victories.keySet().contains(gladiator) || gladiator == individual);
			
			ParetoGameSimulator.runGame(map, individual, gladiator);
		}
	}
	
	private void saveList(File saveFolder, List<Individual> list, int generation) {
		
		File folder = new File(saveFolder.getAbsoluteFile() + "/paretoChampions/");
		if(!folder.exists())
			folder.mkdir();
		
		folder = new File(saveFolder.getAbsoluteFile() + "/paretoChampions/"+generation+"/");
		if(!folder.exists())
			folder.mkdir();
		
		for(int i = 0; i < list.size(); i++) {
			LoadSave.saveAi(new File(saveFolder.getAbsoluteFile() + "/paretoChampions/"+generation+"/" + i), list.get(i).getAiCopy());
		}
	}

	private void runEvolution(File saveFolder) {
		
		population = new ArrayList<Individual>();
		paretoFront = new ArrayList<Individual>();
		oldParetoFront = new ArrayList<Individual>();
		
		System.out.println("Starting");
		
		// Create population
		for(int i = 0; i < POPULATION_SIZE; i++) {
			Ai ai1 = new Ai(map);
			ai1.setRules(Ai.randomRules(random));
			Individual individual = new Individual(ai1, random);
			population.add(individual);
		}
		
		System.out.println("Generated Initial Population");
		
		for(int generation = 0; generation < GENERATIONS; generation++) {
			// Combine paretoFront & population
			List<Individual> combPop = new ArrayList<Individual>();
			
			combPop.addAll(population);
			combPop.addAll(paretoFront);
			
			oldParetoFront.clear();
			
			oldParetoFront.addAll(paretoFront);
			paretoFront.clear();
			population.clear();
			
			// Compete everyone vs S people
			for(Individual challenger:combPop) {
				// Clear any previous victories
				challenger.clear();
				// Make new victories
				runCompetition(challenger, combPop);
			}

			int minimumDominations = 0;
			
			// If the pareto front is too big, shrink it by being more selective
			do {
				minimumDominations++;
				paretoFront.clear();
				for(Individual challenger:combPop) {
					if(challenger.dominates(minimumDominations))
						paretoFront.add(challenger);
				}
			}
			while(paretoFront.size() > PARETO_FRONT_SIZE);
			
			// If no frontier can be established, use the old one
			if(paretoFront.size() == 0)
				paretoFront.addAll(oldParetoFront);
			
			// Fill the remaining space in the population with children of the pareto front
			if(paretoFront.size() != 0)
				for(int i = paretoFront.size(); i < POPULATION_SIZE; i++) {
					population.add(new Individual(paretoFront.get(random.nextInt(paretoFront.size())), paretoFront.get(random.nextInt(paretoFront.size())), random));
				}
			else
				for(int i = 0; i < POPULATION_SIZE; i++) {
					population.add(new Individual(combPop.get(random.nextInt(combPop.size())), combPop.get(random.nextInt(combPop.size())), random));
				}
			
			System.out.println(generation + " pareto size " + paretoFront.size() + " with " + minimumDominations + " min dominations");
			
			saveList(saveFolder, paretoFront, generation);
		}
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
		
		public Map<Individual, Boolean> victories;
		
		public String name;
		private Ai ai;
		private double fitness;
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
		
		public boolean dominates(int limit) {
			int counter = 0;
			
			for(Entry<Individual, Boolean> entry:this.victories.entrySet()) {
				if(entry.getValue())
					counter++;
			}
			
			return counter >= limit;
		}

		public void clear() {
			victories.clear();
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
			this.victories = new HashMap<ParetoEvolver.Individual, Boolean>();
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
			this.victories = new HashMap<ParetoEvolver.Individual, Boolean>();
			
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
	
}
