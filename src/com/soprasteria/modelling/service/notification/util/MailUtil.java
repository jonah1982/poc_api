package com.soprasteria.modelling.service.notification.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil { 
	public boolean sendHtmlMail(EmailInfo emailInfo) {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", emailInfo.getSmtpServer());
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // ä½¿ç”¨JSSEçš„SSL
//		properties.put("mail.smtp.port", emailInfo.getPort());
//		properties.put("mail.smtp.socketFactory.port",emailInfo.getPort());
		
		properties.put("mail.smtp.port", 465);
		properties.put("mail.smtp.socketFactory.port",465);
		properties.put("mail.smtp.socketFactory.fallback", "false"); // å�ªå¤„ç�†SSLçš„è¿žæŽ¥,å¯¹äºŽé�žSSLçš„è¿žæŽ¥ä¸�å�šå¤„ç�†
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.starttls.required","true");
		
		
		Session session = Session.getInstance(properties);
		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);
 
		try {
			// å�‘ä»¶äºº
			Address address = new InternetAddress(emailInfo.getFromUserName());
			message.setFrom(address);
			// æ”¶ä»¶äºº
			Address toAddress = new InternetAddress(emailInfo.getRecipient());
			message.setRecipient(MimeMessage.RecipientType.TO, toAddress); // è®¾ç½®æ”¶ä»¶äºº,å¹¶è®¾ç½®å…¶æŽ¥æ”¶ç±»åž‹ä¸ºTO
 
			// ä¸»é¢˜message.setSubject(changeEncode(emailInfo.getSubject()));
			message.setSubject(emailInfo.getSubject());
			// æ—¶é—´
			message.setSentDate(new Date());
 
			Multipart multipart = new MimeMultipart();
 
			// åˆ›å»ºä¸€ä¸ªåŒ…å�«HTMLå†…å®¹çš„MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// è®¾ç½®HTMLå†…å®¹
			html.setContent(emailInfo.getContent(), "text/html; charset=utf-8");
			multipart.addBodyPart(html);
			// å°†MiniMultipartå¯¹è±¡è®¾ç½®ä¸ºé‚®ä»¶å†…å®¹
			message.setContent(multipart);
			message.saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
 
		try {
			Transport transport = session.getTransport("smtp");
			transport.connect(emailInfo.getSmtpServer(), emailInfo.getFromUserName(), emailInfo.getFromUserPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
 
		return true;
	}
}
