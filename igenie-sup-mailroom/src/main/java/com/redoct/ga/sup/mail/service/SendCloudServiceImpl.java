package com.redoct.ga.sup.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.oct.ga.comm.LogErrorMessage;
import com.redoct.ga.sup.mail.SupMailService;

public class SendCloudServiceImpl
		implements SupMailService
{
	@Override
	public void sendHtml(String fromEmail, String fromName, String toEmail, String toName, String subject, String html)
	{
		try {
			client.sendMail(fromEmail, fromName, toEmail, toName, subject, html);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		} catch (MessagingException e) {
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

		try {
			client.sendMail(fromEmail, fromName, toEmail, toName, subject, html);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		} catch (MessagingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		}
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

		try {
			client.sendMail(fromEmail, fromName, toEmail, toName, subject, html);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		} catch (MessagingException e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		}
	}

	// ////////////////////////////////////////////////////

	private SendCloundClient client;
	private VelocityEngine velocityEngine;// spring define in mail.prpoerties

	public SendCloundClient getClient()
	{
		return client;
	}

	public void setClient(SendCloundClient client)
	{
		this.client = client;
	}

	public VelocityEngine getVelocityEngine()
	{
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine)
	{
		this.velocityEngine = velocityEngine;
	}

	private final static Logger logger = LoggerFactory.getLogger(SendCloudServiceImpl.class);

}
