package com.soprasteria.modelling.service.base.entity;

import java.util.ArrayList;
import java.util.List;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.mongodb.DBObject;

@Entity(name = "role")
public class RoleDomain extends MongoAnnoDomain {
	public static String[] ALL_APPS = new String[] {"map", "site", "dashboard"};
	@Column
	private String role_id;
	@Column
	private String role_name;
	@Column
	private List<String> apps = new ArrayList<>();
	
	public RoleDomain(String roleId) {
		super();
		this.role_id = roleId;
		this.role_name = roleId;
		setOid(roleId);
	}
	
	public RoleDomain(DBObject doc) throws Exception {
		super(doc);
	}
	
	public void setApps(List<String> apps) {this.apps = apps;}
	public List<String> getApps() {return apps;}
	public void addApp(String appId) {if(!apps.contains(appId)) apps.add(appId);}
	public void removeApp(String appId) {if(apps.contains(appId)) apps.remove(appId);}

	public String getRole_name() {return role_name;}
	public void setRole_name(String role_name) {this.role_name = role_name;}
	public String getRole_id() {return role_id;}
}
