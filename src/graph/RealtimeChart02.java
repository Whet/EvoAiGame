package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.XChartPanel;

public class RealtimeChart02 {

	private static final String POPULATION_2_CAPTURES = "Population 2 Captures";
	private static final String POPULATION_2_FRAGS = "Population 2 Frags";
	private static final String POPULATION_1_CAPTURES = "Population 1 Captures";
	private static final String POPULATION_1_FRAGS = "Population 1 Frags";
	private List<Integer> xData;
	private List<Double> yData;

	public static void main(String[] args) {

		// Setup the panel
		final RealtimeChart02 realtimeChart02 = new RealtimeChart02();
		final XChartPanel chartPanel = realtimeChart02.buildPanel();

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// Create and set up the window.
				JFrame frame = new JFrame("XChart");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(chartPanel);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});

		// Simulate a data feed
		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				realtimeChart02.updateData();
				chartPanel.updateSeries(POPULATION_2_CAPTURES, realtimeChart02.getxData(), realtimeChart02.getyData());
				chartPanel.updateSeries(POPULATION_2_FRAGS, realtimeChart02.getxData(), realtimeChart02.getyData());
				chartPanel.updateSeries(POPULATION_1_CAPTURES, realtimeChart02.getxData(), realtimeChart02.getyData());
				chartPanel.updateSeries(POPULATION_1_FRAGS, realtimeChart02.getxData(), realtimeChart02.getyData());
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);

	}

	public XChartPanel buildPanel() {

		return new XChartPanel(getChart());
	}

	public Chart getChart() {

		xData = getMonotonicallyIncreasingData(5);
		yData = getRandomData(5);

		// Create Chart
		Chart chart = new Chart(500, 400);
		chart.setChartTitle("Sample Real-time Chart");
		chart.setXAxisTitle("X");
		chart.setYAxisTitle("Y");
		chart.addSeries(POPULATION_2_CAPTURES, xData, yData);
		chart.addSeries(POPULATION_2_FRAGS, xData, yData);
		chart.addSeries(POPULATION_1_CAPTURES, xData, yData);
		chart.addSeries(POPULATION_1_FRAGS, xData, yData);
		chart.getStyleManager().setChartType(ChartType.Scatter);
		return chart;
	}

	private List<Double> getRandomData(int numPoints) {

		List<Double> data = new ArrayList<Double>();
		for (int i = 0; i < numPoints; i++) {
			data.add(Math.random() * 100);
		}
		return data;
	}

	private List<Integer> getMonotonicallyIncreasingData(int numPoints) {

		List<Integer> data = new ArrayList<Integer>();
		for (int i = 0; i < numPoints; i++) {
			data.add(i);
		}
		return data;
	}

	public void updateData() {

		// Get some new data
		List<Double> newData = getRandomData(1);

		yData.addAll(newData);

		// Limit the total number of points
		while (yData.size() > 20) {
			yData.remove(0);
		}

		xData.add(xData.get(xData.size() - 1) + 1);
		while (xData.size() > 20) {
			xData.remove(0);
		}
	}

	public List<Double> getyData() {

		return yData;
	}

	public List<Integer> getxData() {

		return xData;
	}
}