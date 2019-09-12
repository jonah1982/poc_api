package com.soprasteria.modelling.framework.util;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class StatisticsSort {
	protected Double[] data;
	protected Double mean;
	protected Double var;
	protected Double std;
	protected Double median;
	
	private double cleanRate = 0.01;

	public StatisticsSort(Double[] data) throws Exception {
		if(data.length <= 0) throw new Exception("Too small to cleanup");
		int cleanCount = (int)(data.length*cleanRate);
		Arrays.sort(data);
		
		this.data = new Double[data.length-cleanCount*2];

		for (int i = 0; i < data.length - cleanCount; i++) {
			if(i>=cleanCount) this.data[i-cleanCount]=data[i];
		}
	}
	
	public StatisticsSort(List<Double> data) throws Exception {
		this(data.toArray((new Double[data.size()])));
	}
	
	public int size() {
		return this.data.length;
	}
	
	public Double max() {
		return this.data[data.length-1];
	}
	
	public Double min() {
		return this.data[0];
	}
	
	public Double getMean() throws Exception {
		if (mean == null) {
			double sum = 0.0;
			if (data.length == 0) {
				throw new Exception("normal data data.length is zero");
			}
			for (double a : data) {
					sum += a;
			}
			mean = sum / data.length;
		}
		return mean;
	}

	public Double getVariance() throws Exception {
		if (var == null) {
			double mean = getMean();
			double temp = 0;
			for (double a : data)
				temp += (a - mean) * (a - mean);
			var = temp / data.length;
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
