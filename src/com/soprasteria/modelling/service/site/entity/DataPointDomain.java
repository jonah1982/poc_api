package com.soprasteria.modelling.service.site.entity;

import java.util.HashMap;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.mongodb.DBObject;

@Entity(name = "datapoint")
public class DataPointDomain extends MongoAnnoDomain {
	@Column Object siteid;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private String unit;
	@Column
	private HashMap<String, Object> calibration;
	
	private GenericSiteDomain site;
	
	public static String getSiteOid (String datapointId) {
		return datapointId.substring(0, datapointId.lastIndexOf("-"));
	}
	
	public static String getDataPointType (String datapointId) {
		return datapointId.substring(datapointId.lastIndexOf("-")+1);
	}
	
	public DataPointDomain(DBObject doc) throws Exception {
		super(doc);
	}
	
	public DataPointDomain(GenericSiteDomain site, String type) {
		this.type = type;
		this.name = type;
		this.siteid = site.getOid();
		this.site = site;
		setOid(siteid+"-"+type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Object getSiteid() {
		return siteid;
	}

	public GenericSiteDomain getSite() {
		return site;
	}

	public void setSite(GenericSiteDomain site) {
		this.site = site;
	}

	public HashMap<String, Object> getCalibration() {
		return calibration;
	}

	public void setCalibration(HashMap<String, Object> calibration) {
		this.calibration = calibration;
	}
}
