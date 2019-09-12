package com.soprasteria.modelling.framework.entity;

import com.google.gson.Gson;

public class TableField {
	public final static String TYPE_TEXT = "text";
	public final static String TYPE_LINK = "link";
	
	private String display_name;
	private Object value;
	protected String type = TYPE_TEXT;
	
	public TableField(String display_name, Object value) {
		super();
		this.display_name = display_name;
		this.value = value;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public Object getValue() {
		return value;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
