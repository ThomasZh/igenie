package com.redoct.ga.sup.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.smtp.SMTPTransport;

public class SendCloundClient
{
	private static final String SENDCLOUD_SMTP_HOST = "smtpcloud.sohu.com";
	private static final int SENDCLOUD_SMTP_PORT = 25;
	// ʹ��api_user��api_key������֤
	private static final String API_USER = "Thomas_Zhang_test_XkIUbm";
	private static final String API_KEY = "6wg9LNS4QkawOpip";

	public void sendMail(String fromEmail, String fromName, String toEmail, String toName, String subject, String html)
			throws MessagingException, UnsupportedEncodingException
	{
		// ����javamail
		Properties props = System.getProperties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", SENDCLOUD_SMTP_HOST);
		props.put("mail.smtp.port", SENDCLOUD_SMTP_PORT);
		props.setProperty("mail.smtp.auth", "true");
		props.put("mail.smtp.connectiontimeout", 180);
		props.put("mail.smtp.timeout", 600);
		props.setProperty("mail.mime.encodefilename", "true");

		Session mailSession = Session.getInstance(props, new Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(API_USER, API_KEY);
			}
		});

		SMTPTransport transport = (SMTPTransport) mailSession.getTransport("smtp");

		MimeMessage message = new MimeMessage(mailSession);
		// ������
		message.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
		// �ռ��˵�ַ
		message.addRecipient(RecipientType.TO, new InternetAddress(toEmail));
		// �ʼ�����
		message.setSubject(subject, "UTF-8");

		Multipart multipart = new MimeMultipart("alternative");

		// ���html��ʽ���ʼ�����
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
		contentPart.setHeader("Content-Transfer-Encoding", "base64");
		contentPart.setContent(html, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);
		message.setContent(multipart);
		
		// ����sendcloud�������������ʼ�
		transport.connect();
		logger.debug("connect to sendcloud server, send email");
		transport.sendMessage(message, message.getRecipients(RecipientType.TO));

		String messageId = getMessage(transport.getLastServerResponse());
		String emailId = messageId + "0$" + toEmail;
		logger.debug("messageId:" + messageId);
		logger.debug("emailId:" + emailId);
		transport.close();
	}

	private static String getMessage(String reply)
	{
		String[] arr = reply.split("#");

		String messageId = null;

		if (arr[0].equalsIgnoreCase("250 ")) {
			messageId = arr[1];
		}

		return messageId;
	}

	private final static Logger logger = LoggerFactory.getLogger(SendCloundClient.class);
}
