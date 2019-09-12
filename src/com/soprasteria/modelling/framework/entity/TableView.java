package com.soprasteria.modelling.framework.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class TableView {
	private List<List<TableField>> entries = new ArrayList<>();

	public List<List<TableField>> getEntries() {
		return entries;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
