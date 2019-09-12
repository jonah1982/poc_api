package com.soprasteria.modelling.api.entity;

import java.util.HashMap;

public class DashboardDTO {
	protected String id;
	protected String desc;
	protected HashMap<String, Double> chart;
	public DashboardDTO(String id, String desc, HashMap<String, Double> chart) {
		super();
		this.id = id;
		this.desc = desc;
		this.chart = chart;
	}
}
