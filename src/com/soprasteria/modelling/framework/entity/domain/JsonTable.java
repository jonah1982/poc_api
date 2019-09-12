package com.soprasteria.modelling.framework.entity.domain;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTable {
	private Hashtable<String, JsonObj> table = new Hashtable<>();

	public JsonTable(Collection<JsonObj> coll) {
		if(coll != null)
			for (Iterator<JsonObj> iterator = coll.iterator(); iterator.hasNext();) {
				JsonObj jsonObj = (JsonObj) iterator.next();
				try {
					table.put(jsonObj.getKey(), jsonObj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonTable() {
		
	}
	
	public void updateEntry(JsonObj entry) {
		if(table.size()<100) {
			String key = entry.getKey();
			table.put(key, entry);
		}
	}
	
	public String toJson() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(table.values());
	}
}
