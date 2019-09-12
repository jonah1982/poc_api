package com.soprasteria.modelling.api;

import java.util.HashMap;

import com.soprasteria.modelling.framework.exception.AuthoException;
import org.apache.commons.lang3.ClassUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soprasteria.modelling.api.context.APIContext;
import com.soprasteria.modelling.api.context.APIError;
import com.soprasteria.modelling.api.context.WebServiceAuthManager;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.base.entity.UserDomain;

public class BaseAPI {
	protected APIContext context = new APIContext();
	
	protected WebServiceAuthManager authmanager = new WebServiceAuthManager();
	
	protected HashMap<String, Object> formQuery(String query) {
		context.log("Receive query: "+query);
		HashMap<String, Object> querytable = new HashMap<String, Object>();
		try {
			if(!Tool.isEmpty(query) || !query.toLowerCase().contains("null")) {
				querytable= new Gson().fromJson(query, new TypeToken<HashMap<String, String>>() {
					private static final long serialVersionUID = 3893043968492967462L;}.getType());
			}
		} catch (Exception e) {
			context.log("Parse query failed due to: "+e.getMessage());
		}
		context.log("Extracted query: "+querytable);
		if(querytable == null) querytable = new HashMap<String, Object>();
		return querytable;
	}
	
	protected HashMap<String, String> formProject(String project) {
		context.log("Receive query: "+project);
		HashMap<String, String> projecttable = new HashMap<String, String>();
		try {
			if(!Tool.isEmpty(project) || !project.toLowerCase().contains("null")) {
				projecttable= new Gson().fromJson(project, new TypeToken<HashMap<String, String>>() {
					private static final long serialVersionUID = 3893043968492967462L;}.getType());
			}
		} catch (Exception e) {
			context.log("Parse query failed due to: "+e.getMessage());
		}
		context.log("Extracted query: "+projecttable);
		if(projecttable == null) projecttable = new HashMap<String, String>();
		return projecttable;
	}
	protected String toResponse(Object data) {
		if(data==null){
			APIError response=new APIError();
			response.setErrorCode("1");
			response.setErrorMsg("response is empty: "+data);
			return new Gson().toJson(response);
		}
		
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().disableHtmlEscaping().create();
		return gson.toJson(data);
	}

	/**
	 * return the a simple {property: text} json.
	 * @param property
	 * @param text
	 * @return
	 */
	protected String toSimpleProperJson(String property, String text) {
		JsonObject json = new JsonObject();
		json.addProperty(property, text);
		return json.toString();
	}
	
	protected String toSimpleJson(String property, JsonElement object) {
		JsonObject json = new JsonObject();
		json.add(property, object);
		return json.toString();
	}
	
	protected String toHashMapJson(HashMap<String, Object> map) {
		JsonObject json = new JsonObject();
		for(String s: map.keySet()) {
			Object o = map.get(s);
			if(o == null) continue;
			if(ClassUtils.isPrimitiveOrWrapper(o.getClass()))
				json.addProperty(s, ""+o);
			else
				json.add(s, new Gson().toJsonTree(o));
		}
		return json.toString();
	}
	
	protected void authenNormalUser(String accesstoken) throws Exception {
		context.logDebug("auth token "+accesstoken);
		context.setUser(authmanager.authenUser(accesstoken));
	}
	
	protected void authenAdmin(String accesstoken) throws Exception {
		context.logDebug("auth token "+accesstoken);
		UserDomain user = authmanager.authenUser(accesstoken);
		if(!"admin".equalsIgnoreCase(user.getRole()) && !"superadmin".equalsIgnoreCase(user.getRole())) throw new AuthoException("Admin user is required for this access.");
		context.setUser(user);
	}

	protected void authenSuperAdmin(String accesstoken) throws Exception {
		context.logDebug("auth token "+accesstoken);
		UserDomain user = authmanager.authenUser(accesstoken);
		if(!"superadmin".equalsIgnoreCase(user.getRole())) throw new AuthoException("SuperAdmin user is required for this access.");
		context.setUser(user);
	}
	
	protected void authen(String accesstoken) throws Exception {
		authenNormalUser(accesstoken);
	}
	
	protected void changeContext(String accesstoken, String projectId) throws Exception {
		context.setUser(authmanager.switchProject(accesstoken, projectId));
	}
}
