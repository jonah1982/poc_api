package com.soprasteria.modelling.api.entity;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class GISSummary {
	
	private double total_pipe_length;
	private double max_pipe_diameter;
	private double min_pipe_diameter;
	private List<String> subzones;
	private List<String> layers;
	private Map<Double,Double> diametermap;
	
	public Map<Double, Double> getDiametermap() {
		return diametermap;
	}
	public void setDiametermap(Map<Double, Double> diametermap) {
		this.diametermap = diametermap;
	}
	public double getTotal_pipe_length() {
		return total_pipe_length;
	}
	public void setTotal_pipe_length(double total_pipe_length) {
		this.total_pipe_length = total_pipe_length;
	}
	public double getMax_pipe_diameter() {
		return max_pipe_diameter;
	}
	public void setMax_pipe_diameter(double max_pipe_diameter) {
		this.max_pipe_diameter = max_pipe_diameter;
	}
	public double getMin_pipe_diameter() {
		return min_pipe_diameter;
	}
	public void setMin_pipe_diameter(double min_pipe_diameter) {
		this.min_pipe_diameter = min_pipe_diameter;
	}	
	public List<String> getSubzones() {
		return subzones;
	}
	public void setSubzones(List<String> subzones) {
		this.subzones = subzones;
	}
	public List<String> getLayers() {
		return layers;
	}
	public void setLayers(List<String> layers) {
		this.layers = layers;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
