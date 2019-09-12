package com.soprasteria.modelling.service.base.entity;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;

public class PasswordDomain extends MongoAnnoDomain{
	@Column
	private String userId;
	@Column
	private String password;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
