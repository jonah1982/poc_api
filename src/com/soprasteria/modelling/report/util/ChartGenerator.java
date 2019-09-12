package com.soprasteria.modelling.report.util;

import java.awt.Font;
import java.awt.Rectangle;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;


public class ChartGenerator {
	public static JFreeChart generateBarChart(String title) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.setValue(791, "Demamd", "1750");
		dataSet.setValue(978, "Demamd", "1800");
		dataSet.setValue(1262, "Demamd", "1850");
		dataSet.setValue(1650, "Demamd", "1900");
		dataSet.setValue(2519, "Demamd", "1950");
		dataSet.setValue(6070, "Demamd", "2000");

		JFreeChart chart = ChartFactory.createBarChart(
				title, "hrs", "tcmd",
				dataSet, PlotOrientation.VERTICAL, false, true, false);
		return chart;
	}
	
	public static JFreeChart generateGroupedBarChart( HashMap<String, Double> reading, String name) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		ChartGenerator chartGenerator = new ChartGenerator();
		for(String timestamp : reading.keySet()) {
			Calendar cal = chartGenerator.getDay(timestamp);
			dataset.addValue(reading.get(timestamp), name, cal.get(Calendar.DAY_OF_MONTH)+"  "+
					chartGenerator.getMonthForInt(cal.get(Calendar.MONTH)));
		}
		 JFreeChart chart = ChartFactory.createBarChart(
				 			"", "", "m^3",
				 			dataset, PlotOrientation.VERTICAL, true, true, false);
		 CategoryPlot p = chart.getCategoryPlot();
		 p.getRangeAxis().setAxisLineVisible(false);
		 p.getRangeAxis().setLabelFont(new Font("Dialog", Font.PLAIN, 8));
		 p.getRangeAxis().setTickLabelFont(new Font("Dialog", Font.BOLD, 7));
		 CategoryAxis axis = p.getDomainAxis();
		 axis.setCategoryMargin(0.35);
		 axis.setLabelFont(new Font("Dialog", Font.PLAIN, 3));
		 BarRenderer renderer = (BarRenderer) p.getRenderer();
		 renderer.setBaseLegendShape(new Rectangle(8,8));
		 renderer.setBaseLegendTextFont(new Font("Dialog", Font.PLAIN, 8));
		 renderer.setItemMargin(0.1); 
		 chart.getLegend().setFrame(BlockBorder.NONE);
		 chart.getTitle().setFont(new Font("Dialog", Font.BOLD, 12));
		 axis.setAxisLineVisible(false);
		 axis.setTickLabelFont(new Font("Dialog", Font.BOLD, 6));
		 axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		 axis.setLowerMargin(0.02);
		 axis.setUpperMargin(0.02);
		 chart.getLegend().setItemLabelPadding(new RectangleInsets(5.0, 2.0, 10.0, 15));
		 chart.getLegend().setPadding(new RectangleInsets(45.0, 45.0, 0.0, 0.0)); 
		 return chart;
		}
	
	private Calendar getDay(String timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(timestamp));
		return calendar;
	}
	private String getMonthForInt(int num) {
	        String month = "Invalid";
	        DateFormatSymbols dfs = new DateFormatSymbols();
	        String[] months = dfs.getMonths();
	        if (num >= 0 && num <= 11 ) {
	            month = months[num];
	        }
	        return month;
	 }
}
