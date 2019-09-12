package com.soprasteria.modelling.framework.entity.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonObj {
	public abstract String getKey();
	
	public String toJson() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
}
