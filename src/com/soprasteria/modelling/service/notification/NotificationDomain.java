package com.soprasteria.modelling.service.notification;

import java.util.Date;

import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.service.base.entity.TokenDomain;

@Entity(name = "notification")
public class NotificationDomain  extends MongoAnnoDomain{


	@Column(isNull = false, isKey = true)
	private Date time;

	@Column
	private Long ts;

	@Column
	private String mode;//phone,email

	@Column
	private String sender;

	@Column
	protected String recipient;

	@Column
	private String subject;

	@Column
	private String content;

	@Column
	private String status;//new,success,fail


	private TokenDomain token;
	public NotificationDomain( DBObject doc) throws Exception {
		super(doc);
	}
	public NotificationDomain(String toUser,String subject,String content) {
		super();
		setOid(String.valueOf(new Date().getTime()));
		this.recipient = toUser;
		this.subject = subject;
		this.content = content;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}



	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}


	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public TokenDomain getToken() {
		return token;
	}


	public void setToken(TokenDomain token) {
		this.token = token;
	}

	public static String toOid(Date timestamp) throws Exception {
		return ""+timestamp.getTime();
	}

}
