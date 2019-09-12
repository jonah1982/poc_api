package com.soprasteria.modelling.framework.entity;

public class TableLinkField extends TableField {
	protected String link;
	
	public TableLinkField(String display_name, Object value, String link) {
		super(display_name, value);
		type = TYPE_LINK;
		this.link = link;
	}
}
