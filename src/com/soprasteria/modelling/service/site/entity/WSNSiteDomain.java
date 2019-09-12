package com.soprasteria.modelling.service.site.entity;

import java.util.ArrayList;
import java.util.List;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.mongodb.DBObject;

@Entity(name = "site")
public class WSNSiteDomain extends GenericSiteDomain {
	public static String SITE_TYPE = "wsn";
	public static String SITE_ICON = "siteicons/wsnsite.png";
	public static String DATAPOINT_PRESSURE = "pressure";
	public static String DATAPOINT_BATTERY = "battery";
	
	@Column
	private List<String> neighbors;
	@Column
	private List<String> nearby;
	@Column
	private long checkpoint_reading;
	@Column
	private double reading;
	@Column
	private double dataquality;
	@Column
	private double datavariation;
	@Column
	private double maxmagnitude;
	@Column
	private double maxconfidence;
	@Column
	private String maxmageventid;
	@Column
	private String maxconeventid;
	private double healthLevel = 1; 
	
	public WSNSiteDomain(String siteId, String deviceRef) throws Exception {
		super(siteId, deviceRef);
		type = SITE_TYPE;
		icon = SITE_ICON;
	}

	public WSNSiteDomain(DBObject doc) throws Exception {
		super(doc);
	}
	
	public double getMaxmagnitude() {
		return maxmagnitude;
	}

	public void setMaxmagnitude(double maxmagnitude) {
		this.maxmagnitude = maxmagnitude;
	}

	public double getMaxconfidence() {
		return maxconfidence;
	}

	public void setMaxconfidence(double maxconfidence) {
		this.maxconfidence = maxconfidence;
	}

	public String getMaxmageventid() {
		return maxmageventid;
	}

	public void setMaxmageventid(String maxmageventid) {
		this.maxmageventid = maxmageventid;
	}

	public String getMaxconeventid() {
		return maxconeventid;
	}

	public void setMaxconeventid(String maxconeventid) {
		this.maxconeventid = maxconeventid;
	}

	public long getCheckpoint_reading() {
		return checkpoint_reading;
	}

	public void setCheckpoint_reading(long checkpoint_reading) {
		this.checkpoint_reading = checkpoint_reading;
	}

	public double getReading() {
		return reading;
	}

	public void setReading(double reading) {
		this.reading = reading;
	}

	public double getDataquality() {
		return dataquality;
	}

	public void setDataquality(double dataquality) {
		this.dataquality = dataquality;
	}

	public double getDatavariation() {
		return datavariation;
	}

	public void setDatavariation(double datavariation) {
		this.datavariation = datavariation;
	}

	public List<String> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<String> neighbors) {
		this.neighbors = neighbors;
	}

	public void addNeighbors(String siteid) {	
		if(neighbors == null) neighbors = new ArrayList<String>();
		if(!neighbors.contains(siteid)) neighbors.add(siteid);
	}
	
	public void setIdle() {
		icon = "siteicon/orange.png";
//		setDeviceRef("");
		status = STATUS_IDLE;
	}

	public double getHealthLevel() {
		return healthLevel;
	}

	public void setHealthLevel(double healthLevel) {
		this.healthLevel = healthLevel;
	}

	public List<String> getNearby() {
		return nearby;
	}

	public void setNearby(List<String> nearby) {
		this.nearby = nearby;
	}
	
}
