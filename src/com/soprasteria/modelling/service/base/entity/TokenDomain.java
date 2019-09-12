package com.soprasteria.modelling.service.base.entity;

import java.util.Date;
import java.util.UUID;

import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.framework.util.DateTimeTool;

@Entity(name = "accesstoken")
public class TokenDomain extends MongoAnnoDomain {
	public static Long TOKEN_EXP_TIME = DateTimeTool.convertResolutionTime("1y");
	@Column
	private String userid;
	@Column
	private String projectid;
	@Column
	private String roleid;
	
	private ProjectDomain project;
	
	public TokenDomain() {
		super();
		setOid(UUID.randomUUID().toString());
	}
	
	public TokenDomain(String token) {
		super();
		setOid(token);
	}
	
	public TokenDomain(DBObject doc) throws Exception {
		super(doc);
	}

	public String getToken() {return getOid().toString();}

	public String getUserid() {return userid;}

	public void setUserid(String userid) {this.userid = userid;}
	
	public String getProjectid() {return projectid;}

	public void setProjectid(String projectid) {this.projectid = projectid;}

	public ProjectDomain getProject() {return project;}

	public void setProject(ProjectDomain project) {this.project = project;}

	public String getRoleid() {return roleid;}

	public void setRoleid(String roleid) {this.roleid = roleid;}

	public boolean isValid() {
		return userid != null && (new Date().getTime() - getLastmod().getTime()) < TOKEN_EXP_TIME;
	}
	
	
}
