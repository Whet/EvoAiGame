package graph;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JFrame;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.XChartPanel;

import evolution.Evolver.Individual;

public class Graph {

	private static final String POPULATION_2_CAPTURES = "Population 2 Captures";
	private static final String POPULATION_2_FRAGS = "Population 2 Frags";
	private static final String POPULATION_1_CAPTURES = "Population 1 Captures";
	private static final String POPULATION_1_FRAGS = "Population 1 Frags";
	
	private List<Double> fragScore1;
	private List<Double> captureScore1;
	private List<Double> fragScore2;
	private List<Double> captureScore2;
	private List<Integer> time;
	
	private XChartPanel killGraph1;
	private XChartPanel flagGraph1;
	private XChartPanel killGraph2;
	private XChartPanel flagGraph2;
	
	public Graph() {
		
		fragScore1 = new ArrayList<>();
		captureScore1 = new ArrayList<>();
		fragScore2 = new ArrayList<>();
		captureScore2 = new ArrayList<>();
		time = new ArrayList<>();
		
		this.fragScore1.add(0.0);
		this.captureScore1.add(0.0);
		this.fragScore2.add(0.0);
		this.captureScore2.add(0.0);
		this.time.add(0);
		
		Chart chart = new ChartBuilder().width(800).height(400).theme(ChartTheme.Matlab).build();
	    chart.getStyleManager().setChartType(ChartType.Scatter);
	    chart.getStyleManager().setMarkerSize(5);
	    chart.getStyleManager().setLegendVisible(false);
	    
	    chart.addSeries(POPULATION_1_FRAGS, fragScore1, fragScore1);
		
		chart.setXAxisTitle("Generations");
		chart.setYAxisTitle("Score");
		chart.setChartTitle("Population 1 Damage Given");
		
		killGraph1 = new XChartPanel(chart);
		
		chart = new ChartBuilder().width(800).height(400).theme(ChartTheme.Matlab).build();
	    chart.getStyleManager().setChartType(ChartType.Scatter);
	    chart.getStyleManager().setMarkerSize(5);
	    chart.getStyleManager().setLegendVisible(false);
	    
		chart.addSeries(POPULATION_1_CAPTURES, captureScore1, captureScore1);
		
		chart.setXAxisTitle("Generations");
		chart.setYAxisTitle("Score");
		chart.setChartTitle("Population 1 Flag Captures");
		
		flagGraph1 = new XChartPanel(chart);
		
		chart = new ChartBuilder().width(800).height(400).theme(ChartTheme.Matlab).build();
	    chart.getStyleManager().setChartType(ChartType.Scatter);
	    chart.getStyleManager().setMarkerSize(5);
	    chart.getStyleManager().setLegendVisible(false);
	    
		chart.addSeries(POPULATION_2_FRAGS, fragScore2, fragScore2);
		
		chart.setXAxisTitle("Generations");
		chart.setYAxisTitle("Score");
		chart.setChartTitle("Population 2 Damage Given");
		
		killGraph2 = new XChartPanel(chart);
		
		chart = new ChartBuilder().width(800).height(400).theme(ChartTheme.Matlab).build();
	    chart.getStyleManager().setChartType(ChartType.Scatter);
	    chart.getStyleManager().setMarkerSize(5);
	    chart.getStyleManager().setLegendVisible(false);
	    
		chart.addSeries(POPULATION_2_CAPTURES, captureScore2, captureScore2);
		
		chart.setXAxisTitle("Generations");
		chart.setYAxisTitle("Score");
		chart.setChartTitle("Population 2 Flag Captures");
		
		flagGraph2 = new XChartPanel(chart);
		
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// Create and set up the window.
				JFrame frame = new JFrame("Genetic Data");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new GridLayout(2, 2));
				frame.add(killGraph1);
				frame.add(flagGraph1);
				frame.add(killGraph2);
				frame.add(flagGraph2);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	public void updateData(PriorityQueue<Individual> population1, PriorityQueue<Individual> population2, int generation) {

		Iterator<Individual> iterator = population1.iterator();
		List<Double> captures1 = new ArrayList<>();
		List<Double> frags1 = new ArrayList<>();
		
		while(iterator.hasNext()) {
			Individual next = iterator.next();
			captures1.add(next.getAverageFlagScore());
			frags1.add(next.getAverageDamageDealt());
		}
		
		captureScore1.addAll(captures1);
		fragScore1.addAll(frags1);
		
		iterator = population2.iterator();
		List<Double> captures2 = new ArrayList<>();
		List<Double> frags2 = new ArrayList<>();
		
		while(iterator.hasNext()) {
			Individual next = iterator.next();
			captures2.add(next.getAverageFlagScore());
			frags2.add(next.getAverageDamageDealt());
		}
		
		captureScore2.addAll(captures2);
		fragScore2.addAll(frags2);
		
		for(int i = 0; i < population1.size(); i++) {
			this.time.add(generation);
		}
		
		killGraph1.updateSeries(POPULATION_1_FRAGS, this.time, getFragData1());
		flagGraph1.updateSeries(POPULATION_1_CAPTURES, this.time, getFlagData1());
		killGraph2.updateSeries(POPULATION_2_FRAGS, this.time, getFragData2());
		flagGraph2.updateSeries(POPULATION_2_CAPTURES, this.time, getFlagData2());

	}

	public List<Double> getFragData1() {
		return this.fragScore1;
	}
	
	public List<Double> getFlagData1() {
		return this.captureScore1;
	}
	
	public List<Double> getFragData2() {
		return this.fragScore2;
	}
	
	public List<Double> getFlagData2() {
		return this.captureScore2;
	}
	
}
