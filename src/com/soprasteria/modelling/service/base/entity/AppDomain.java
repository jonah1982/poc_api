package com.soprasteria.modelling.service.base.entity;

import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;

@Entity(name = "app")
public class AppDomain extends MongoAnnoDomain {
	public final static String STATUS_ENABLE = "enable";
	public final static String STATUS_DISABLE = "disable";
	public final static String STATUS_ENABLE4SUPER = "enable4super";
	
	@Column
	private String name;
	@Column
	private String appfile;
	@Column
	private String status;
	@Column
	private String parent;
	@Column
	private String init; // initialization script method for the app
	@Column
	private String icon;
	
	public AppDomain(String appid) {
		super();
		setOid(appid.toLowerCase());
		setStatus(STATUS_ENABLE);
	}
	public AppDomain(DBObject doc) throws Exception {
		super(doc);
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getAppfile() {return appfile;}
	public void setAppfile(String appfile) {this.appfile = appfile;}
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	public String getParent() {return parent;}
	public void setParent(String parent) {this.parent = parent;}
	public String getInit() {return init;}
	public void setInit(String init) {this.init = init;}
	public String getIcon() {return icon;}
	public void setIcon(String icon) {this.icon = icon;}
}
