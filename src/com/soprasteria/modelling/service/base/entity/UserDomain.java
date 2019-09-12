package com.soprasteria.modelling.service.base.entity;

import java.util.HashMap;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.mongodb.DBObject;

@Entity(name = "user")
public class UserDomain extends MongoAnnoDomain {
	/*@Column
	private String userId;*/
	@Column(isNull = false, isKey = true)
	private String username;
	@Column
	private transient String password;
	@Column
	private String role;
	@Column
	private String lang;
	@Column
	private String timezone;
	@Column
	private HashMap<String, String> projects;
	@Column
	private String phone;
	@Column
	private String email;
	@Column
	private HashMap<String, Object> access;
	@Column
	private String status;

	@Column
	private String verificationcode;
	
	private TokenDomain token;

	public UserDomain(String username) {
		super(); 
		this.username = username.toLowerCase(); 
		if(projects == null) projects = new HashMap<>();
		lang = "zh-CHS";
		timezone = "Asia/Shanghai";
	}

	public UserDomain(DBObject doc) throws Exception {super(doc); if(projects == null) projects = new HashMap<>();}

	public String getUsername() {return username;}

	public String getPassword() {return password;}

	public void setPassword(String password) {this.password = password;}

	public String getLang() {return lang;}

	public String getTimezone() {return timezone;}

	public void setTimezone(String timezone) {this.timezone = timezone;}

	public void setLang(String lang) {this.lang = lang;}

	public String getRole() {return role;}

	public void setRole(String role) {this.role = role;}

	public HashMap<String, String> getProjects() {return projects;}

	public void setProjects(HashMap<String, String> projects) {this.projects = projects;}
	
	public TokenDomain getToken() {return token;}

	public void setToken(TokenDomain token) {this.token = token;}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public HashMap<String, Object> getAccess() {
		return access;
	}

	public void setAccess(HashMap<String, Object> access) {
		this.access = access;
	}
	public String getVerificationcode() {
		return verificationcode;
	}

	public void setVerificationcode(String verificationcode) {
		this.verificationcode = verificationcode;
	}
	
}
