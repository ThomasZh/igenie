package com.oct.ga.apns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.service.GaOfflineNotifyService;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;

public class ApnsServiceImpl
		implements GaOfflineNotifyService
{
	public ApnsServiceImpl()
	{
	}

	@Override
	public void sendMessage(boolean isOnline, String apnsToken, int badgenum, MessageInlinecast message)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");
		String txt = "";
		if (message.getContent() != null && message.getContent().length() > 0) {
			if (message.getContent().length() > 200)
				txt = message.getContent().substring(0, 200) + "...";
			else
				txt = message.getContent();
		}

		payloadBuilder.customField("type", GlobalArgs.CONTENT_TYPE_TXT);
		payloadBuilder.customField("id", message.getChannelId());

		payloadBuilder.localizedKey("MESSAGE_SENDTO_YOU");
		payloadBuilder.localizedArguments(message.getChannelName(), message.getFromAccountName(), txt);

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("A message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendInvite(boolean isOnline, String apnsToken, int badgenum, GaInvite invite)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");

		payloadBuilder.localizedKey("INVITE_MESSAGE_OTHERS_APPLY_FRIEND");
		payloadBuilder.localizedArguments(invite.getFromAccountName());

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("An invite message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendInviteFeedback(boolean isOnline, String apnsToken, int badgenum, GaInviteFeedback feedback)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");

		switch (feedback.getFeedbackState()) {
		case GlobalArgs.INVITE_STATE_ACCPET:
			payloadBuilder.localizedKey("INVITE_MESSAGE_OTHER_ACCEPT_FRIEND");
			break;
		case GlobalArgs.INVITE_STATE_REJECT:
			payloadBuilder.localizedKey("INVITE_MESSAGE_OTHER_REJECT_FRIEND");
			break;
		}

		payloadBuilder.localizedArguments(feedback.getFeedbackUserName());

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("An invite feedback message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendActivityJoin(boolean isOnline, String apnsToken, int badgenum, String activityName,
			String memberName)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");

		payloadBuilder.localizedKey("ACTIVITY_JOIN");
		payloadBuilder.localizedArguments(activityName, memberName);

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("An activity join message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendTaskLog(boolean isOnline, String apnsToken, int badgenum, NotifyTaskLog log)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		String payload = null;

		String content = "";
		switch (log.getActivityState()) {
		case GlobalArgs.TASK_ACTION_ADD:
			content = "TASK_ACTION_ADD";
			break;
		case GlobalArgs.TASK_ACTION_DELETE:
			content = "TASK_ACTION_DELETE";
			break;
		case GlobalArgs.TASK_ACTION_MODIFY:
			content = "TASK_ACTION_MODIFY";
			break;
		case GlobalArgs.TASK_ACTION_COMPLETED: {
			content = "TASK_ACTION_COMPLETED";

			PayloadBuilder payloadBuilder = APNS.newPayload();
			payloadBuilder.badge(badgenum);
			payloadBuilder.sound("default");
			payloadBuilder.localizedKey(content);
			payloadBuilder.localizedArguments(log.getChannelId(), log.getFromAccountName());
			payload = payloadBuilder.toString();
			logger.debug("Payload json: " + payload);
			break;
		}
		}

		logger.info("A task log message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendApplyState(boolean isOnline, String apnsToken, int badgenum, GaApplyStateNotify notify)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");

		payloadBuilder.customField("type", notify.getAction());
		payloadBuilder.customField("id", notify.getChannelId());

		switch (notify.getAction()) {
		case GlobalArgs.INVITE_STATE_ACCPET:
			payloadBuilder.localizedKey("APPLY_MESSAGE_ACCEPT");
			break;
		case GlobalArgs.INVITE_STATE_APPLY:
			payloadBuilder.localizedKey("APPLY_MESSAGE_APPLY");
			break;
		case GlobalArgs.INVITE_STATE_JOIN:
			payloadBuilder.localizedKey("APPLY_MESSAGE_JOIN");
			break;
		case GlobalArgs.INVITE_STATE_REFILL:
			payloadBuilder.localizedKey("APPLY_MESSAGE_REFILL");
			break;
		case GlobalArgs.INVITE_STATE_REJECT:
			payloadBuilder.localizedKey("APPLY_MESSAGE_REJECT");
			break;
		}

		payloadBuilder.localizedArguments(notify.getChannelName(), notify.getFromAccountName());

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("An apply notify message sent to (" + apnsToken + ") through by apns.");
		service.push(apnsToken, payload);
	}

	@Override
	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, MsgFlowBasicInfo nofity)
	{
		if (service == null) {
			GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
					.getBean("globalConfigurationVariables");
			String apnsCertificateDestination = gcv.getApnsCertificateDestination();
			String apnsCertificatePath = gcv.getApnsCertificatePath();
			String apnsCertificateCode = gcv.getApnsCertificateCode();

			if (apnsCertificateDestination.equals("development")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			} else if (apnsCertificateDestination.equals("production")) {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode)
						.withAppleDestination(true).build();
			} else {
				service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withSandboxDestination()
						.build();
			}
		}

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(badgenum);
		payloadBuilder.sound("default");

		payloadBuilder.customField("type", nofity.getActionTag());
		payloadBuilder.customField("id", nofity.getChannelId());

		switch (nofity.getActionTag()) {
		case GlobalArgs.TASK_ACTION_ADD: // create
			payloadBuilder.localizedKey("TASK_ACTION_ADD");
			break;
		case GlobalArgs.TASK_ACTION_RECOMMEND: // recommend
			payloadBuilder.localizedKey("TASK_ACTION_RECOMMEND");
			break;
		case GlobalArgs.TASK_ACTION_ADD_ATTACH: // moment
			payloadBuilder.localizedKey("TASK_ACTION_ADD_ATTACH");
			break;
		case GlobalArgs.TASK_ACTION_MOMENT_FAVORITE:
			payloadBuilder.localizedKey("TASK_ACTION_MOMENT_FAVORITE");
			break;
		case GlobalArgs.TASK_ACTION_MOMENT_COMMENT:
			payloadBuilder.localizedKey("TASK_ACTION_MOMENT_COMMENT");
			break;
		case GlobalArgs.TASK_ACTION_CHANGE_TIME:
			payloadBuilder.localizedKey("TASK_ACTION_CHANGE_TIME");
			break;
		case GlobalArgs.TASK_ACTION_CANCELED:
			payloadBuilder.localizedKey("TASK_ACTION_CANCELED");
			break;
		case GlobalArgs.TASK_ACTION_COMPLETED:
			payloadBuilder.localizedKey("TASK_ACTION_COMPLETED");
			break;
		case GlobalArgs.TASK_ACTION_UNCOMPLETED:
			payloadBuilder.localizedKey("TASK_ACTION_UNCOMPLETED");
			break;
		case GlobalArgs.TASK_ACTION_JOIN:
			payloadBuilder.localizedKey("TASK_ACTION_JOIN");
			break;
		case GlobalArgs.TASK_ACTION_QUIT:
			payloadBuilder.localizedKey("TASK_ACTION_QUIT");
			break;
		case GlobalArgs.TASK_ACTION_APPLY:
			payloadBuilder.localizedKey("TASK_ACTION_APPLY");
			break;
		case GlobalArgs.TASK_ACTION_ACCEPT:
			payloadBuilder.localizedKey("TASK_ACTION_ACCEPT");
			break;
		case GlobalArgs.TASK_ACTION_REJECT:
			payloadBuilder.localizedKey("TASK_ACTION_REJECT");
			break;
		case GlobalArgs.TASK_ACTION_REFILL:
			payloadBuilder.localizedKey("TASK_ACTION_REFILL");
			break;
		case GlobalArgs.TASK_ACTION_KICKOUT_MEMBER:
			payloadBuilder.localizedKey("TASK_ACTION_KICKOUT_MEMBER");
			break;
		}

		payloadBuilder.localizedArguments(nofity.getChannelName(), nofity.getFromAccountName());

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		logger.info("An apply notify message sent to (" + notifyToken + ") through by apns.");
		service.push(notifyToken, payload);
	}

	// ////////////////////////////////////////////////////////////

	private ApnsService service;

	public ApnsService getService()
	{
		return service;
	}

	public void setService(ApnsService service)
	{
		this.service = service;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApnsServiceImpl.class);

}
