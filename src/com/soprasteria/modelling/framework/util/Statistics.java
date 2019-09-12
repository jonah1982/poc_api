package com.soprasteria.modelling.framework.util;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Statistics {
	protected Double[] data;
	protected int size;
	protected Double mean;
	protected Double var;
	protected Double std;
	protected Double median;

	public Statistics(Double[] data) {
		this.data = data;
		size = this.data.length;
	}

	public Statistics(List<Double> data) {
		this(data.toArray((new Double[data.size()])));
	}

	public Statistics(int size, Double mean, Double var, Double std, Double median) {
		super();
		this.size = size;
		this.mean = mean;
		this.var = var;
		this.std = std;
		this.median = median;
	}

	public Double getMean() throws Exception {
		if (mean == null) {
			double sum = 0.0;
			if (size == 0) {
				throw new Exception("normal data size is zero");
			}
			for (double a : data) {
					sum += a;
			}
			mean = sum / size;
		}
		return mean;
	}

	public Double getVariance() throws Exception {
		if (var == null) {
			double mean = getMean();
			double temp = 0;
			for (double a : data)
				temp += (a - mean) * (a - mean);
			var = temp / size;
		}
		return var;
	}

	public Double getStdDev() throws Exception {
		if (std == null) {
			std = Math.sqrt(getVariance());
		}
		return std;
	}

	public Double median() {
		if (median == null) {
			Arrays.sort(data);

			if (data.length % 2 == 0) {
				return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
			}
			median = data[data.length / 2];
		}
		return median;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
