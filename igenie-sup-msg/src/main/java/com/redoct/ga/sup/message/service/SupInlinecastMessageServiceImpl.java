package com.redoct.ga.sup.message.service;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.inlinecast.InlinecastActivityJoinReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastApplyStateReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastInviteFeedbackReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastInviteReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastMessageReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskLogReq;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaOfflineNotifyService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.GateSession;
import com.redoct.ga.sup.session.domain.StpSession;

public class SupInlinecastMessageServiceImpl
		extends InlinecastMessageServiceIf
{
	@Override
	public void multicast(ApplicationContext context, MessageInlinecast msg)
			throws IOException, InterruptedException, SupSocketException
	{
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		String toUserId = msg.getToAccountId();
		logger.debug("send message to=[" + toUserId + "]");

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		logger.debug("message to=[" + toUserId + "] device=[" + deviceId + "] osVersion=[" + osVersion
				+ "] notifyToken=[" + notifyToken + "]");
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);
		if (gateSession == null) {
			logger.warn("stp=[null] has no session=[" + ioSessionId + "] as userId=[" + toUserId + "] of deviceId=["
					+ deviceId + "]");
			return;
		} else {
			logger.debug("to=[" + toUserId + "] device=[" + deviceId + "] on stp=[" + gateSession.getStpId() + "] ip=["
					+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");
		}

		if (stpSession.isActive()) { // online
			logger.debug("to=[" + toUserId + "] device=[" + deviceId + "] is online");

			if (osVersion.toLowerCase().contains("ios")) {
				msg.setReciverIoSessionId(ioSessionId);
				InlinecastMessageReq reqCmd = new InlinecastMessageReq(msg);
				// TlvObject tlvReq = reqCmd.encode();

				logger.info("frined session=[" + ioSessionId + "] is online another STP: userId(" + toUserId
						+ "] of deviceId=[" + deviceId + "] on STP=[" + gateSession.getStpIp() + ":"
						+ gateSession.getStpPort() + "]");

				try {
					InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
					socketClient.sendStpCommand(addr, reqCmd);
				} catch (Exception e) {
					logger.error("can't send online message to another stp: " + LogErrorMessage.getFullInfo(e));
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendMessage(true, jpushAlias, badgeNum, msg);
				logger.debug("send online message to=[" + toUserId + "] android device[" + deviceId
						+ "] by jpush alias[" + jpushAlias + "]");
			}
		} else { // offline
			logger.debug("to=[" + toUserId + "] device=[" + deviceId + "] is offline");

			if (groupService.queryDndMode(msg.getChatId(), toUserId) == GlobalArgs.DND_YES) {
				logger.info("user=[" + toUserId + "] set do not distribe on chat=[" + msg.getChatId() + "]");
			} else {
				if (osVersion.toLowerCase().contains("ios")) {
					if (notifyToken != null && notifyToken.length() > 0) {
						logger.info("friend session=[" + ioSessionId + "] offline, send apns: userId=[" + toUserId
								+ "] of device=[" + deviceId + "]");

						GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
						apnsService.sendMessage(false, notifyToken, badgeNum, msg);
					} else {
						logger.warn("friend session=[" + ioSessionId + "] offline, and no token: userId=[" + toUserId
								+ "] of device=[" + deviceId + "]");
					}
				} else { // android
					GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
					String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

					jpushService.sendMessage(false, jpushAlias, badgeNum, msg);
					logger.debug("send offline message to=[" + toUserId + "] android device[" + deviceId
							+ "] by jpush alias[" + jpushAlias + "]");
				}
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, GaInvite invite)
			throws IOException, InterruptedException, SupSocketException
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = invite.getToUserSemiId();
		String toUserName = "";

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);

		if (stpSession.isActive()) { // online
			if (osVersion.toLowerCase().contains("ios")) {
				if (gateSession == null) {
					logger.warn("stp=[null] has no session=[" + ioSessionId + "] as username=[" + toUserName
							+ "] userId=[" + toUserId + "] of deviceId=[" + deviceId + "]");
				} else {
					// Logic: send to MQ; another stp will send it
					// to friend. send a transmit message to another
					// stp.
					InlinecastInviteReq reqCmd = new InlinecastInviteReq(ioSessionId, invite);

					logger.info("frined session=[" + ioSessionId + "] is online another STP: username=[" + toUserName
							+ "] userId(" + toUserId + "] of deviceId=[" + deviceId + "] on STP=["
							+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");

					try {
						InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
						socketClient.sendStpCommand(addr, reqCmd);
					} catch (Exception e) {
						logger.error("can't send online invite to another stp: " + LogErrorMessage.getFullInfo(e));
					}
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendInvite(true, jpushAlias, badgeNum, invite);
				logger.debug("send online invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		} else { // offline
			if (osVersion.toLowerCase().contains("ios")) {
				if (notifyToken != null && notifyToken.length() > 0) {
					logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

					GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
					apnsService.sendInvite(false, notifyToken, badgeNum, invite);
				} else {
					logger.warn("friend session=[" + ioSessionId + "] offline, and no token: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendInvite(false, jpushAlias, badgeNum, invite);
				logger.debug("send offline invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, GaInviteFeedback feedback)
			throws IOException, InterruptedException, SupSocketException
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = feedback.getFromUserId();
		String toUserName = "";

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);

		if (stpSession.isActive()) { // online
			if (osVersion.toLowerCase().contains("ios")) {
				if (gateSession == null) {
					logger.warn("stp=[null] has no session=[" + ioSessionId + "] as username=[" + toUserName
							+ "] userId=[" + toUserId + "] of deviceId=[" + deviceId + "]");
				} else {
					// Logic: send to MQ; another stp will send it
					// to friend. send a transmit message to another
					// stp.
					InlinecastInviteFeedbackReq reqCmd = new InlinecastInviteFeedbackReq(ioSessionId, feedback);

					logger.info("frined session=[" + ioSessionId + "] is online another STP: username=[" + toUserName
							+ "] userId(" + toUserId + "] of deviceId=[" + deviceId + "] on STP=["
							+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");

					try {
						InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
						socketClient.sendStpCommand(addr, reqCmd);
					} catch (Exception e) {
						logger.error("can't send online invite feedback to another stp: "
								+ LogErrorMessage.getFullInfo(e));
					}
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendInviteFeedback(true, jpushAlias, badgeNum, feedback);
				logger.debug("send online invite feedback message to " + toUserName + "[" + toUserId
						+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		} else { // offline
			if (osVersion.toLowerCase().contains("ios")) {
				if (notifyToken != null && notifyToken.length() > 0) {
					logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

					GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
					apnsService.sendInviteFeedback(false, notifyToken, badgeNum, feedback);
				} else {
					logger.warn("friend session=[" + ioSessionId + "] offline, and no token: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendInviteFeedback(false, jpushAlias, badgeNum, feedback);
				logger.debug("send offline invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, String activityName, String leaderId, String memberName,
			int timestamp)
			throws IOException, InterruptedException, SupSocketException
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = leaderId;
		String toUserName = "";

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);

		if (stpSession.isActive()) { // online
			if (osVersion.toLowerCase().contains("ios")) {
				if (gateSession == null) {
					logger.warn("stp=[null] has no session=[" + ioSessionId + "] as username=[" + toUserName
							+ "] userId=[" + toUserId + "] of deviceId=[" + deviceId + "]");
				} else {
					// Logic: send to MQ; another stp will send it
					// to friend. send a transmit message to another
					// stp.
					InlinecastActivityJoinReq reqCmd = new InlinecastActivityJoinReq(ioSessionId, activityName,
							leaderId, memberName, timestamp);

					logger.info("frined session=[" + ioSessionId + "] is online another STP: username=[" + toUserName
							+ "] userId(" + toUserId + "] of deviceId=[" + deviceId + "] on STP=["
							+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");

					try {
//						InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
//						socketClient.sendStpCommand(addr, reqCmd);
					} catch (Exception e) {
						logger.error("can't send online apply to another stp: " + LogErrorMessage.getFullInfo(e));
					}
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendActivityJoin(true, jpushAlias, badgeNum, activityName, memberName);
				logger.debug("send activity join message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		} else { // offline
			if (osVersion.toLowerCase().contains("ios")) {
				if (notifyToken != null && notifyToken.length() > 0) {
					logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

					GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
					apnsService.sendActivityJoin(false, notifyToken, badgeNum, activityName, memberName);
				} else {
					logger.warn("friend session=[" + ioSessionId + "] offline, and no token: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendActivityJoin(false, notifyToken, badgeNum, activityName, memberName);
				logger.debug("send offline activity join message to " + toUserName + "[" + toUserId
						+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, GaApplyStateNotify notify)
			throws IOException, InterruptedException, SupSocketException
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = notify.getToAccountId();
		String toUserName = "";
		int currentTimestamp = DatetimeUtil.currentTimestamp();

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);

		if (stpSession.isActive()) { // online
			if (osVersion.toLowerCase().contains("ios")) {
				if (gateSession == null) {
					logger.warn("stp=[null] has no session=[" + ioSessionId + "] as username=[" + toUserName
							+ "] userId=[" + toUserId + "] of deviceId=[" + deviceId + "]");
				} else {
					// Logic: send to MQ; another stp will send it
					// to friend. send a transmit message to another
					// stp.
					InlinecastApplyStateReq reqCmd = new InlinecastApplyStateReq(ioSessionId, notify);

					logger.info("frined session=[" + ioSessionId + "] is online another STP: username=[" + toUserName
							+ "] userId(" + toUserId + "] of deviceId=[" + deviceId + "] on STP=["
							+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");

					try {
						InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
						socketClient.sendStpCommand(addr, reqCmd);
					} catch (Exception e) {
						logger.error("can't send online apply to another stp: " + LogErrorMessage.getFullInfo(e));
					}
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendApplyState(true, jpushAlias, badgeNum, notify);
				logger.debug("send online invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");

				// sync state received
				GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
				applyService.modifySyncStateToReceived(notify.getFromAccountId(), notify.getToAccountId(),
						notify.getChannelId(), currentTimestamp);
			}
		} else { // offline
			if (osVersion.toLowerCase().contains("ios")) {
				if (notifyToken != null && notifyToken.length() > 0) {
					logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

					GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
					apnsService.sendApplyState(false, notifyToken, badgeNum, notify);
				} else {
					logger.warn("friend session=[" + ioSessionId + "] offline, and no token: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendApplyState(false, jpushAlias, badgeNum, notify);
				logger.debug("send offline invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		}
	}
	
	@Override
	public void multicast(ApplicationContext context, MsgFlowBasicInfo notify)
			throws IOException, InterruptedException, SupSocketException
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = notify.getToActionAccountId();
		String toUserName = "";
		int currentTimestamp = DatetimeUtil.currentTimestamp();

		StpSession stpSession = supSessionService.queryStpSession(toUserId);
		if (stpSession == null) { // offline & not in cache
			return;
		}

		String osVersion = stpSession.getDeviceOsVersion();
		String deviceId = stpSession.getDeviceId();
		String notifyToken = stpSession.getNotifyToken();
		long ioSessionId = stpSession.getIoSessionId();
		int badgeNum = badgeNumService.countBadgeNum(toUserId);
		GateSession gateSession = supSessionService.queryGateSession(deviceId);

		if (stpSession.isActive()) { // online
			if (osVersion.toLowerCase().contains("ios")) {
				if (gateSession == null) {
					logger.warn("stp=[null] has no session=[" + ioSessionId + "] as username=[" + toUserName
							+ "] userId=[" + toUserId + "] of deviceId=[" + deviceId + "]");
				} else {
					// Logic: send to MQ; another stp will send it
					// to friend. send a transmit message to another
					// stp.
					InlinecastTaskLogReq reqCmd = new InlinecastTaskLogReq(ioSessionId, notify);

					logger.info("frined session=[" + ioSessionId + "] is online another STP: username=[" + toUserName
							+ "] userId(" + toUserId + "] of deviceId=[" + deviceId + "] on STP=["
							+ gateSession.getStpIp() + ":" + gateSession.getStpPort() + "]");

					try {
						InetSocketAddress addr = new InetSocketAddress(gateSession.getStpIp(), gateSession.getStpPort());
						socketClient.sendStpCommand(addr, reqCmd);
					} catch (Exception e) {
						logger.error("can't send online task log to another stp: " + LogErrorMessage.getFullInfo(e));
					}
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendTaskLog(true, jpushAlias, badgeNum, notify);
				logger.debug("send online invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");

				// sync state received
				GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
				applyService.modifySyncStateToReceived(notify.getFromAccountId(), notify.getToActionAccountId(),
						notify.getChannelId(), currentTimestamp);
			}
		} else { // offline
			if (osVersion.toLowerCase().contains("ios")) {
				if (notifyToken != null && notifyToken.length() > 0) {
					logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

					GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
					apnsService.sendTaskLog(false, notifyToken, badgeNum, notify);
				} else {
					logger.warn("friend session=[" + ioSessionId + "] offline, and no token: username=[" + toUserName
							+ "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
				}
			} else { // android
				GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
				String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

				jpushService.sendTaskLog(false, jpushAlias, badgeNum, notify);
				logger.debug("send offline invite message to " + toUserName + "[" + toUserId + "] android device["
						+ deviceId + "] by jpush alias[" + jpushAlias + "]");
			}
		}
	}

	// /////////////////////////////////////////////////////

	private SupSocketClient socketClient;

	public SupSocketClient getSocketClient()
	{
		return socketClient;
	}

	public void setSocketClient(SupSocketClient socketClient)
	{
		this.socketClient = socketClient;
	}

	private final static Logger logger = LoggerFactory.getLogger(SupInlinecastMessageServiceImpl.class);

}
