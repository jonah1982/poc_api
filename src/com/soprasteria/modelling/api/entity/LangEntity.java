package com.soprasteria.modelling.api.entity;

public class LangEntity {
	private String context;
	private String text_id;
	private LangBody origin;
	private LangBody target;
	
	public LangEntity (String context, String textId, String englishtext) {
		this.context = context;
		this.text_id = textId;
		this.origin = new LangBody("en", englishtext);
		this.target = origin;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getText_id() {
		return text_id;
	}

	public void setText_id(String text_id) {
		this.text_id = text_id;
	}

	public String getOrigin() {
		return origin.getText();
	}
	
	public String getTarget() {
		return target.getText();
	}

	public void setTarget(String lang, String text) {
		this.target = new LangBody(lang, text);
	}
}

class LangBody {
	private String lang;
	private String text;
	
	public LangBody(String lang, String text) {
		this.lang = lang;
		this.text = text;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
