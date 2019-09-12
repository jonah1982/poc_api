package com.soprasteria.modelling.service.base.entity;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.framework.util.DateTimeTool;
import com.soprasteria.modelling.framework.util.Tool;
import com.mongodb.DBObject;

@Entity(name = "project")
public class ProjectDomain extends MongoAnnoDomain {
	
	public static String TYPE_WSN = "wsn";
	@Column
	private String name;
	@Column
	private String timezone;
	@Column
	private String type;
	@Column
	private Date start_time;
	@Column
	private Date end_time;
	@Column
	private String address;
	@Column 
	private String status;
	@Column
	private String client_name;
	@Column
	private HashMap<String, Object> map;
	@Column
	private HashMap<String, Object> sensors;
	
	public ProjectDomain(String projectId) throws Exception {
		if(!StringUtils.isAlphanumeric(projectId)) throw new Exception("Project ID must be alphanumeric: "+projectId);
		setOid(projectId.toLowerCase());
		if(Tool.isEmpty(type)) type = TYPE_WSN;
		map = new HashMap<>();
	}
	
	public ProjectDomain(DBObject doc) throws Exception {
		super(doc);
		if(Tool.isEmpty(type)) type = TYPE_WSN;
		if(map == null) map = new HashMap<>();
	}
	
	public String getTimezone() {return timezone;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}	
	public String getAddress() {return address;}
	public void setAddress(String address) {this.address = address;}
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	public Date getStart_time() {return start_time;}
	public void setStart_time(String start_timestr) {this.start_time = DateTimeTool.convertToTimeStamp(start_timestr);}
	public Date getEnd_time() {return end_time;}
	public void setEnd_time(String end_timestr) {this.end_time = DateTimeTool.convertToTimeStamp(end_timestr);}
	public String getClient_name() {return client_name;}
	public void setClient_name(String client_name) {this.client_name = client_name;}
	
	public void setTimezone(String timezone) throws Exception {
		if(DateTimeTool.validTimeZone(timezone))
			this.timezone = timezone;
		else
			throw new Exception("Invalid timezone id: "+timezone);
	}
	
	public void setMapInfo(double lat, double lng, int level) {
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("level", level);
	}

	public HashMap<String, Object> getMap() {
		return map;
	}

	public HashMap<String, Object> getSensors() {
		return sensors;
	}

	public void setSensorCount(String sensortype, Integer count) {
		if(this.sensors == null) this.sensors = new HashMap<String, Object>();
		this.sensors.put(sensortype, count);
	}
}
