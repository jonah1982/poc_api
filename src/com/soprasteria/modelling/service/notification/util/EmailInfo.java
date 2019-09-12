package com.soprasteria.modelling.service.notification.util;

import com.soprasteria.modelling.service.notification.NotificationDomain;

public class EmailInfo extends NotificationDomain{
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private String smtpServer = "smtp.mathearth.com"; 
// SMTP服务器地址
	private String port = "25"; // 端口
	private String fromUserName = "support@mathearth.com"; 
// 登录SMTP服务器的用户名,发送人邮箱地址
	private String fromUserPassword = "Mathearth123@"; 
// 登录SMTP服务器的密码
	
	public EmailInfo(String recipient, String subject, String content) {
		super(recipient, subject, content);
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getFromUserPassword() {
		return fromUserPassword;
	}
	public void setFromUserPassword(String fromUserPassword) {
		this.fromUserPassword = fromUserPassword;
	}
	public String getSSL_FACTORY() {
		return SSL_FACTORY;
	}
}

