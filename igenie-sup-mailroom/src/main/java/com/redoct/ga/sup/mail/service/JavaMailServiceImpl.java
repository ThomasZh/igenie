package com.redoct.ga.sup.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.oct.ga.comm.LogErrorMessage;
import com.redoct.ga.sup.mail.SupMailService;

public class JavaMailServiceImpl
		implements SupMailService
{
	@Override
	public void sendHtml(String fromEmail, String fromName, String toEmail, String toName, String subject, String html)
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		try {
			messageHelper.setFrom(fromEmail, fromName);
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(subject);
			messageHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		} catch (UnsupportedEncodingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		}
	}

	@Override
	public void sendForgotPwd(String toEmail, String toName, String ekey)
	{
		String subject = "TripC2C Support: password recovery";
		String templateFileName = "mail-recovery-pwd-en.vm";
		String fromEmail = "no-reply@tripc2c.com";
		String fromName = "TripC2C Support";

		Map<String, String> model = new HashMap<String, String>();
		model.put("to-name", toName);
		model.put("to-email", toEmail);
		model.put("ekey", ekey);

		String html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFileName, "UTF-8", model);
		logger.debug(html);

		this.sendHtml(fromEmail, fromName, toEmail, toName, subject, html);
	}

	@Override
	public void sendFriendInvite(String fromEmail, String fromName, String toEmail, String toName, String ekey)
	{
		String subject = "TripC2C Support: friend invite";
		String templateFileName = "mail-invite-register-en.vm";

		Map<String, String> model = new HashMap<String, String>();
		model.put("from-name", fromName);
		model.put("from-email", fromEmail);
		model.put("to-name", toName);
		model.put("to-email", toEmail);
		model.put("ekey", ekey);

		String html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFileName, "UTF-8", model);
		logger.debug(html);

		this.sendHtml(fromEmail, fromName, toEmail, toName, subject, html);
	}

	private JavaMailSender mailSender;// spring define in mail.prpoerties
	private VelocityEngine velocityEngine;// spring define in mail.prpoerties

	private final static Logger logger = LoggerFactory.getLogger(JavaMailServiceImpl.class);

}
