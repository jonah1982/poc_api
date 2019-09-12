package com.soprasteria.modelling.framework.entity;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Location implements Serializable {
	public static String LOCATION_LATITUDE = "lat";
	public static String LOCATION_LONGITUDE = "lng";
	
	private static final long serialVersionUID = -3924591682510141055L;
	private HashMap<String, Object> loc = new HashMap<>();
	
	public Location(String latlng) throws Exception {
		this(Double.parseDouble(latlng.split(",")[0]), Double.parseDouble(latlng.split(",")[1]));
	}
	
	public Location(Double lat, Double lng) {
		loc.put(LOCATION_LATITUDE, lat);
		loc.put(LOCATION_LONGITUDE, lng);
	}

	public DBObject getLoc() {
		DBObject doc = new BasicDBObject();
		doc.put("loc", loc);
		return doc;
	}
	
	public HashMap<String, Object> getLocTable() {
		return loc;
	}
	
	public String toString() {
		return loc.get(LOCATION_LATITUDE)+","+loc.get(LOCATION_LONGITUDE);
	}

	public Double getLat() {
		return (Double)loc.get(LOCATION_LATITUDE);
	}

	public Double getLng() {
		return (Double)loc.get(LOCATION_LONGITUDE);
	}
}
