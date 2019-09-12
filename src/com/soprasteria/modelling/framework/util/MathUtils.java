package com.soprasteria.modelling.framework.util;

import java.text.DecimalFormat;
import java.util.List;

public class MathUtils {
	public static double getAverageValue(List<Double> values, int decimalPlaces) {
		double average = 0;
		if (values != null && values.size() > 0) {
			double total = 0;
			for (double element : values) {
				total += element;
			}
			average = total / values.size();
		}
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(average));
	}
}
