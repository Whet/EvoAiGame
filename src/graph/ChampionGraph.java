package graph;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.XChartPanel;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;

public class ChampionGraph {

	private static Chart getChart(String graphTitle, List<Integer> yData) {

		// generates Log data
		List<Integer> xData = new ArrayList<Integer>();
		for (int i = 0; i < yData.size(); i++) {
			xData.add(i);
		}

		// Create Chart
		Chart chart = new ChartBuilder().width(800).height(600).build();

		chart.getStyleManager().setChartType(ChartType.Line);
	    chart.getStyleManager().setMarkerSize(5);
	    chart.getStyleManager().setLegendVisible(false);
	    
	    chart.addSeries("Champion Scores", xData, yData);
		
		chart.setXAxisTitle("Champion Generation");
		chart.setYAxisTitle("Wins");
		chart.setChartTitle(graphTitle);

		return chart;
	}

	public static void showChampionGraph(final List<Integer> wins1, final List<Integer> wins2) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// Create and set up the window.
				JFrame frame = new JFrame("Champions Graph");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new GridLayout(2, 2));
				frame.add(new XChartPanel(getChart("Population 1 Champion Victories vs Other Champions in the Population", wins1)));
				frame.add(new XChartPanel(getChart("Population 2 Champion Victories vs Other Champions in the Population", wins2)));

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
