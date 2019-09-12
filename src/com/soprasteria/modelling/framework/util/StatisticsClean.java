package com.soprasteria.modelling.framework.util;

import java.util.Arrays;
import java.util.List;

public class StatisticsClean extends Statistics {
	private int cleanNum = 2;

	public StatisticsClean(Double[] data) {
		super(data);
	}

	public StatisticsClean(List<Double> data) {
		super(data);
	}

	public StatisticsClean(int size, Double mean, Double var, Double std, Double median) {
		super(size, median, median, median, median);
	}

	public Double getMean() throws Exception {
		if (mean == null) {
			double sum = 0.0;
			if (size == 0) {
				throw new Exception("normal data size is zero");
			}
			if (size > cleanNum * 2) {
				Arrays.sort(data);
				int sizeclean = 0;
				if (size > cleanNum * 2) {
					for (int i = cleanNum; i < data.length - cleanNum; i++) {
						sum += data[i];
						sizeclean++;
					}
					size = sizeclean;
				} else {
					for (double a : data) {
						sum += a;
					}
				}
				mean = sum / size;
			}
		}
		return mean;
	}

	public Double getVariance() throws Exception {
		if (var == null) {
			double mean = getMean();
			double temp = 0;

			if (size > cleanNum * 2) {
				Arrays.sort(data);
				int sizeclean = 0;
				for (int i = cleanNum; i < data.length - cleanNum; i++) {
					temp += (data[i] - mean) * (data[i] - mean);
					sizeclean++;
				}
				size = sizeclean;
			} else {
				for (double a : data)
					temp += (a - mean) * (a - mean);
			}
			var = temp / size;
		}
		return var;
	}

}
