package com.soprasteria.modelling.service.messaging;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
	private String sender;
	private String smtpHost;
	private int smtpPort;
	private String smtppasswd;
	private Properties properties;
	private MimeMessage message;

	public EmailService(String sender, String smtpHost, int smtpPort) {
		this.sender = sender;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		properties = System.getProperties();
		properties.setProperty("mail.smtp.host", this.smtpHost);
		properties.put("mail.smtp.port", this.smtpPort);
		properties.put("mail.smtp.localhost", "waterwise.pub.gov.sg");
	}

	public EmailService(String sender, String smtpHost, int smtpPort, String password) {
		this(sender, smtpHost, smtpPort);
		this.smtppasswd = password;
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
	}

	private void init() throws AddressException, MessagingException {
		Session session = null;
		if(smtppasswd == null)
			session = Session.getDefaultInstance(properties);
		else {
			session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender, smtppasswd);
				}
			});
		}

		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(this.sender));
	}

	public void send(String recipient, String subject, String text) throws MessagingException {
		init();
		message.setRecipients(Message.RecipientType.TO, recipient);
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);
		System.out.println("Sent message successfully....");
	}
}
