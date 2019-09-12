package com.soprasteria.modelling.service.lang;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.framework.util.Tool;

@Entity(name = "lang")
public class LangDomain extends MongoAnnoDomain {
	public final static String FIELD_TRANSLATION		=	"translation"; 
	@Column
	private String text;
	@Column
	private HashMap<String, String> translation = new HashMap<>();

	public LangDomain(String context, String txtId) {
		super();
		if(!Tool.isEmpty(context) && !context.equalsIgnoreCase("default")) setOid(context.toLowerCase()+"-"+txtId.toLowerCase());
		else setOid(txtId.toLowerCase());
	}
	
	public LangDomain(DBObject doc) throws Exception {super(doc);}

	public String getText() {return text;}

	public void setText(String text) {this.text = text;}
	
	public void addText(String lang, String text) {
		translation.put(lang, text);
	}
	
	public String getText(String lang) {
		String translatedText = text;
		try {
			translatedText = translation.get(lang);
		} catch(Exception e) {}
		return translatedText;
	}
	
	public DBObject toSetStmt() {
		DBObject doc1 = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();

		String[] keys = (String[]) translation.keySet().toArray(new String[0]);

		for (String key : keys) {
			doc2.put(FIELD_TRANSLATION+"."+key, translation.get(key));
		}

		doc1.put("$set", doc2);
		return doc1;
	}
}
