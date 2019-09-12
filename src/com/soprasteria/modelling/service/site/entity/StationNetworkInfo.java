package com.soprasteria.modelling.service.site.entity;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StationNetworkInfo {
	public static String FIELD_ZONE = "supply_zone";
	public static String FIELD_JUNCTION = "junction_id";
	public static String FIELD_INP = "inp_file";
	public static String FIELD_LATLNG = "latlng";

	private String supply_zone;
	private String junction_id;
	private String latlng;
	private String inp_file;

	public StationNetworkInfo(String supply_zone, String junction_id) {
		this(supply_zone, junction_id, supply_zone+".inp");
	}

	public StationNetworkInfo(String supply_zone, String junction_id, String inpFile){
		setSupply_zone(supply_zone);
		setJunction_id(junction_id);
		setInpFile(inpFile);
	}

	public static StationNetworkInfo getInstance(HashMap<String, String> domain) {
		Type type = new TypeToken<StationNetworkInfo>(){}.getType();
		StationNetworkInfo obj = new Gson().fromJson(new Gson().toJson(domain), type);
		return obj;
	}
	
	public HashMap<String, String> toMap() {
		Type type = new TypeToken<HashMap<String, String>>(){}.getType();
		HashMap<String, String> map = new Gson().fromJson(new Gson().toJson(this), type);
		
		return map;
	}

	public String getSupply_zone() {
		return supply_zone;
	}

	public void setSupply_zone(String supply_zone) {
		this.supply_zone = supply_zone;
	}

	public String getJunction_id() {
		return junction_id;
	}

	public void setJunction_id(String junction_id) {
		this.junction_id = junction_id;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getInpFile() {
		return inp_file;
	}

	public void setInpFile(String inpFile) {
		this.inp_file = inpFile;
	}
}


