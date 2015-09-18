package com.oct.ga.inlinecast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.apply.SyncApplyStateResp;
import com.oct.ga.comm.cmd.inlinecast.InlinecastApplyStateReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastInviteFeedbackReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastInviteReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastMessageReq;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.cmd.msg.SyncMessageResp;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaOfflineNotifyService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.oct.ga.session.GaSessionInfo;
import com.oct.ga.session.SessionService3MapImpl;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;

// Unicast,Multicast,Broadcast
public class InlinecastMessageServiceImpl
		extends InlinecastMessageServiceIf
{
	@Override
	public void multicast(ApplicationContext context, MessageInlinecast msg)
			throws UnsupportedEncodingException, InterruptedException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		String toUserId = msg.getToAccountId();
		String toUserName = msg.getToAccountName();

		Set<String> deviceSet = sessionService.getDeviceList(toUserId);
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String deviceId = it.next();
			logger.debug("to deviceId: " + deviceId);

			// do not send to my this device(session) again.
			if (deviceId.equals(msg.getSenderDeviceId())) {
				continue;
			}

			GaSessionInfo gaSession = sessionService.getSession(deviceId);
			if (gaSession == null) {
				logger.warn("no session login after MemCached reset: username(" + toUserName + ") userId(" + toUserId
						+ ") deviceId(" + deviceId + ")");
				break;
			}
			long ioSessionId = gaSession.getIoSessionId();
			logger.debug("to session=[" + ioSessionId + "] username=[" + toUserName + "] userId=[" + toUserId
					+ "] deviceId=[" + deviceId + "]");

			String osVersion = gaSession.getOsVersion();
			if (osVersion == null) {
				;
			} else {
				String apnsToken = gaSession.getApnsToken();
				int badgeNum = badgeNumService.countBadgeNum(toUserId);

				if (osVersion.toLowerCase().contains("ios")) {
					if (sessionService.isOnline(toUserId, deviceId)) {
						// on same stp
						if (gcv.getStpId().equals(gaSession.getStpId())) {
							IoSession ioSession = this.getIoService().getManagedSessions().get(ioSessionId);
							// this session is offline now!
							if (ioSession == null) {
								sessionService.inactive(toUserId, deviceId);
								continue; // next device
							}

							SyncMessageResp syncMessageResp = new SyncMessageResp(msg);
							logger.debug("Send online message's sync state: " + msg.getSyncState());
							TlvObject tlvSyncMessageResp = CommandParser.encode(syncMessageResp);

							WriteFuture future = ioSession.write(tlvSyncMessageResp);
							// Wait until the message is completely written out
							// to the O/S buffer.
							future.awaitUninterruptibly();
							if (future.isWritten()) {
								// The message has been written successfully.
								logger.info("friend session=[" + ioSessionId + "] is online, username=[" + toUserName
										+ "] userId=[" + toUserId + "] deviceId=[" + deviceId + "] write msg success!");
							} else {
								// The messsage couldn't be written out
								// completely for some reason. (e.g. Connection
								// is closed)
								logger.warn("friend session=[" + ioSessionId + "] write msg fail: username=["
										+ toUserName + "] userId=[" + toUserId + "] deviceId=[" + deviceId + "]");

								// LOGIC: clean this session from sessionMap.
								sessionService.inactive(toUserId, deviceId);
								ioSession.close(true);
							}
						} else { // on other stp
							StpServerInfoJsonBean stp = sessionService.getStp(gaSession.getStpId());
							if (stp == null) {
								logger.warn("stp=[" + gaSession.getStpId() + "] has no session=[" + ioSessionId
										+ "] as username=[" + toUserName + "] userId=[" + toUserId + "] of deviceId=["
										+ deviceId + "]");

								if (groupService.queryDndMode(msg.getChatId(), toUserId) == GlobalArgs.DND_YES) {
									logger.info("user=[" + toUserId + "] set do not distribe on chat=["
											+ msg.getChatId() + "]");
									continue;
								}

								// LOGIC: send a offline message through apns
								if (apnsToken != null && apnsToken.length() > 0) {
									logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=["
											+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

									GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
											.getBean("gaApnsService");
									apnsService.sendMessage(false, apnsToken, badgeNum, msg);
								} else {
									logger.warn("friend session=[" + ioSessionId
											+ "] offline, and no token: username=[" + toUserName + "] userId=["
											+ toUserId + "] of device=[" + deviceId + "]");
								}
							} else {
								// Logic: send to MQ; another stp will send it
								// to friend. send a transmit message to another
								// stp.
								msg.setReciverIoSessionId(ioSessionId);
								InlinecastMessageReq reqCmd = new InlinecastMessageReq(msg);
								TlvObject tlvReq = reqCmd.encode();

								logger.info("frined session=[" + ioSessionId + "] is online another STP: username=["
										+ toUserName + "] userId(" + toUserId + "] of deviceId=[" + deviceId
										+ "] on STP=[" + stp.getServerIp() + ":" + stp.getPort() + "]");

								InlinecastSocketManager socket = GenericSingleton
										.getInstance(InlinecastSocketManager.class);
								socket.sendto(stp.getServerIp(), stp.getPort(), tlvReq);
							}
						}
					} else { // offline
						if (groupService.queryDndMode(msg.getChatId(), toUserId) == GlobalArgs.DND_YES) {
							logger.info("user=[" + toUserId + "] set do not distribe on chat=[" + msg.getChatId() + "]");
							continue;
						}

						// LOGIC: send a offline message through apns
						if (apnsToken != null && apnsToken.length() > 0) {
							logger.info("friend session=[" + gaSession.getIoSessionId()
									+ "] offline, to apns: username=[" + toUserName + "] userId=[" + toUserId
									+ "] of device=[" + deviceId + "]");

							GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
									.getBean("gaApnsService");
							apnsService.sendMessage(false, apnsToken, badgeNum, msg);
						} else {
							logger.warn("friend session=[" + gaSession.getIoSessionId()
									+ "] offline, and no token: username=[" + toUserName + "] userId=[" + toUserId
									+ "] of device=[" + deviceId + "]");
						}
					}
				} else { // android
					GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
					String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

					if (sessionService.isOnline(toUserId, deviceId)) {
						jpushService.sendMessage(true, jpushAlias, badgeNum, msg);
						logger.debug("send online message to " + toUserName + "[" + toUserId + "] android device["
								+ deviceId + "] by jpush alias[" + jpushAlias + "]");
					} else {
						if (groupService.queryDndMode(msg.getChatId(), toUserId) == GlobalArgs.DND_NO) {
							jpushService.sendMessage(false, jpushAlias, badgeNum, msg);
							logger.debug("send offline message to " + toUserName + "[" + toUserId + "] android device["
									+ deviceId + "] by jpush alias[" + jpushAlias + "]");
						}
					}
				}
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, GaInvite invite)
			throws UnsupportedEncodingException, InterruptedException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = invite.getToUserSemiId();
		String toUserName = "";

		Set<String> deviceSet = sessionService.getDeviceList(toUserId);
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String deviceId = it.next();
			logger.debug("to deviceId: " + deviceId);

			GaSessionInfo gaSession = sessionService.getSession(deviceId);
			if (gaSession == null) {
				logger.warn("no session login after MemCached reset: username(" + toUserName + ") userId(" + toUserId
						+ ") deviceId(" + deviceId + ")");
				break;
			}
			long ioSessionId = gaSession.getIoSessionId();
			logger.debug("to session=[" + ioSessionId + "] username=[" + toUserName + "] userId=[" + toUserId
					+ "] deviceId=[" + deviceId + "]");

			String osVersion = gaSession.getOsVersion();
			if (osVersion == null) {
				;
			} else {
				String apnsToken = gaSession.getApnsToken();
				int badgeNum = badgeNumService.countBadgeNum(toUserId);

				if (osVersion.toLowerCase().contains("ios")) {
					if (sessionService.isOnline(toUserId, deviceId)) {
						// on same stp
						if (gcv.getStpId().equals(gaSession.getStpId())) {
							IoSession ioSession = this.getIoService().getManagedSessions().get(ioSessionId);
							// this session is offline now!
							if (ioSession == null) {
								sessionService.inactive(toUserId, deviceId);
								continue; // next device
							}

							List<GaInvite> array = new ArrayList<GaInvite>();
							array.add(invite);
							SyncInviteResp syncInviteResp = new SyncInviteResp(ErrorCode.SUCCESS, array, null);
							TlvObject tlvSyncInviteResp = CommandParser.encode(syncInviteResp);

							WriteFuture future = ioSession.write(tlvSyncInviteResp);
							// Wait until the message is completely written out
							// to the O/S buffer.
							future.awaitUninterruptibly();
							if (future.isWritten()) {
								// The message has been written successfully.
								logger.info("friend session=[" + ioSessionId + "] is online, username=[" + toUserName
										+ "] userId=[" + toUserId + "] deviceId=[" + deviceId
										+ "] write invite success!");
							} else {
								// The messsage couldn't be written out
								// completely for some reason. (e.g. Connection
								// is closed)
								logger.warn("friend session=[" + ioSessionId + "] write invite fail: username=["
										+ toUserName + "] userId=[" + toUserId + "] deviceId=[" + deviceId + "]");

								// LOGIC: clean this session from sessionMap.
								sessionService.inactive(toUserId, deviceId);
								ioSession.close(true);
							}
						} else { // on other stp
							StpServerInfoJsonBean stp = sessionService.getStp(gaSession.getStpId());
							if (stp == null) {
								logger.warn("stp=[" + gaSession.getStpId() + "] has no session=[" + ioSessionId
										+ "] as username=[" + toUserName + "] userId=[" + toUserId + "] of deviceId=["
										+ deviceId + "]");

								// LOGIC: send a offline message through apns
								if (apnsToken != null && apnsToken.length() > 0) {
									logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=["
											+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

									GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
											.getBean("gaApnsService");
									apnsService.sendInvite(false, apnsToken, badgeNum, invite);
								} else {
									logger.warn("friend session=[" + ioSessionId
											+ "] offline, and no token: username=[" + toUserName + "] userId=["
											+ toUserId + "] of device=[" + deviceId + "]");
								}
							} else {
								// Logic: send to MQ; another stp will send it
								// to friend. send a transmit message to another
								// stp.
								InlinecastInviteReq reqCmd = new InlinecastInviteReq(ioSessionId, invite);
								TlvObject tlvReq = reqCmd.encode();

								logger.info("frined session=[" + ioSessionId + "] is online another STP: username=["
										+ toUserName + "] userId(" + toUserId + "] of deviceId=[" + deviceId
										+ "] on STP=[" + stp.getServerIp() + ":" + stp.getPort() + "]");

								InlinecastSocketManager socket = GenericSingleton
										.getInstance(InlinecastSocketManager.class);
								socket.sendto(stp.getServerIp(), stp.getPort(), tlvReq);
							}
						}
					} else { // offline
						GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
						apnsService.sendInvite(false, apnsToken, badgeNum, invite);

						logger.info("friend session=[" + gaSession.getIoSessionId() + "] offline, to apns: username=["
								+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
					}
				} else { // android
					GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
					String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

					if (sessionService.isOnline(toUserId, deviceId)) {
						jpushService.sendInvite(true, jpushAlias, badgeNum, invite);
						logger.debug("send online invite message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					} else {
						jpushService.sendInvite(false, jpushAlias, badgeNum, invite);
						logger.debug("send offline invite message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					}
				}
			}
		}
	}

	@Override
	public void multicast(ApplicationContext context, GaInviteFeedback feedback)
			throws UnsupportedEncodingException, InterruptedException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = feedback.getFromUserId();
		String toUserName = "";

		Set<String> deviceSet = sessionService.getDeviceList(toUserId);
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String deviceId = it.next();
			logger.debug("to deviceId: " + deviceId);

			GaSessionInfo gaSession = sessionService.getSession(deviceId);
			if (gaSession == null) {
				logger.warn("no session login after MemCached reset: username(" + toUserName + ") userId(" + toUserId
						+ ") deviceId(" + deviceId + ")");
				break;
			}
			long ioSessionId = gaSession.getIoSessionId();
			logger.debug("to session=[" + ioSessionId + "] username=[" + toUserName + "] userId=[" + toUserId
					+ "] deviceId=[" + deviceId + "]");

			String osVersion = gaSession.getOsVersion();
			if (osVersion == null) {
				;
			} else {
				String apnsToken = gaSession.getApnsToken();
				int badgeNum = badgeNumService.countBadgeNum(toUserId);

				if (osVersion.toLowerCase().contains("ios")) {
					if (sessionService.isOnline(toUserId, deviceId)) {
						// on same stp
						if (gcv.getStpId().equals(gaSession.getStpId())) {
							IoSession ioSession = this.getIoService().getManagedSessions().get(ioSessionId);
							// this session is offline now!
							if (ioSession == null) {
								sessionService.inactive(toUserId, deviceId);
								continue; // next device
							}

							List<GaInviteFeedback> array = new ArrayList<GaInviteFeedback>();
							array.add(feedback);
							SyncInviteResp syncInviteResp = new SyncInviteResp(ErrorCode.SUCCESS, null, array);
							TlvObject tlvSyncInviteResp = CommandParser.encode(syncInviteResp);

							WriteFuture future = ioSession.write(tlvSyncInviteResp);
							// Wait until the message is completely written out
							// to the O/S buffer.
							future.awaitUninterruptibly();
							if (future.isWritten()) {
								// The message has been written successfully.
								logger.info("friend session=[" + ioSessionId + "] is online, username=[" + toUserName
										+ "] userId=[" + toUserId + "] deviceId=[" + deviceId
										+ "] write invite success!");
							} else {
								// The messsage couldn't be written out
								// completely for some reason. (e.g. Connection
								// is closed)
								logger.warn("friend session=[" + ioSessionId + "] write invite fail: username=["
										+ toUserName + "] userId=[" + toUserId + "] deviceId=[" + deviceId + "]");

								// LOGIC: clean this session from sessionMap.
								sessionService.inactive(toUserId, deviceId);
								ioSession.close(true);
							}
						} else { // on other stp
							StpServerInfoJsonBean stp = sessionService.getStp(gaSession.getStpId());
							if (stp == null) {
								logger.warn("stp=[" + gaSession.getStpId() + "] has no session=[" + ioSessionId
										+ "] as username=[" + toUserName + "] userId=[" + toUserId + "] of deviceId=["
										+ deviceId + "]");

								// LOGIC: send a offline message through apns
								apnsToken = gaSession.getApnsToken();
								if (apnsToken != null && apnsToken.length() > 0) {
									logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=["
											+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

									badgeNum = badgeNumService.countBadgeNum(toUserId);
									GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
											.getBean("gaApnsService");
									apnsService.sendInviteFeedback(false, apnsToken, badgeNum, feedback);
								} else {
									logger.warn("friend session=[" + ioSessionId
											+ "] offline, and no token: username=[" + toUserName + "] userId=["
											+ toUserId + "] of device=[" + deviceId + "]");
								}
							} else {
								// Logic: send to MQ; another stp will send it
								// to friend. send a transmit message to another
								// stp.
								InlinecastInviteFeedbackReq reqCmd = new InlinecastInviteFeedbackReq(ioSessionId,
										feedback);
								TlvObject tlvReq = reqCmd.encode();

								logger.info("frined session=[" + ioSessionId + "] is online another STP: username=["
										+ toUserName + "] userId(" + toUserId + "] of deviceId=[" + deviceId
										+ "] on STP=[" + stp.getServerIp() + ":" + stp.getPort() + "]");

								InlinecastSocketManager socket = GenericSingleton
										.getInstance(InlinecastSocketManager.class);
								socket.sendto(stp.getServerIp(), stp.getPort(), tlvReq);
							}
						}
					} else { // offline
						// LOGIC: send a offline message through apns
						if (apnsToken != null && apnsToken.length() > 0) {
							GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
									.getBean("gaApnsService");
							apnsService.sendInviteFeedback(false, apnsToken, badgeNum, feedback);

							logger.info("friend session=[" + gaSession.getIoSessionId()
									+ "] offline, to apns: username=[" + toUserName + "] userId=[" + toUserId
									+ "] of device=[" + deviceId + "]");
						} else {
							logger.warn("friend session=[" + gaSession.getIoSessionId()
									+ "] offline, and no token: username=[" + toUserName + "] userId=[" + toUserId
									+ "] of device=[" + deviceId + "]");

							continue;
						}
					}
				} else { // android
					GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
					String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

					if (sessionService.isOnline(toUserId, deviceId)) {
						jpushService.sendInviteFeedback(true, jpushAlias, badgeNum, feedback);
						logger.debug("send online invite feedback message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					} else {
						jpushService.sendInviteFeedback(false, jpushAlias, badgeNum, feedback);
						logger.debug("send offline invite feedback message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					}
				}
			}
		}
	}

//	@Override
//	public void multicast(ApplicationContext context, String activityName, String leaderId, String memberName)
//			throws UnsupportedEncodingException, InterruptedException
//	{
//		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
//		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
//		String toUserId = leaderId;
//		String toUserName = "";
//
//		Set<String> deviceSet = sessionService.getDeviceList(toUserId);
//		if (deviceSet == null)
//			return;
//		Iterator<String> it = deviceSet.iterator();
//		while (it.hasNext()) {
//			String deviceId = it.next();
//			logger.debug("to deviceId: " + deviceId);
//
//			GaSessionInfo gaSession = sessionService.getSession(deviceId);
//			if (gaSession == null) {
//				logger.warn("no session login after MemCached reset: username(" + toUserName + ") userId(" + toUserId
//						+ ") deviceId(" + deviceId + ")");
//				break;
//			}
//			long ioSessionId = gaSession.getIoSessionId();
//			logger.debug("to session=[" + ioSessionId + "] username=[" + toUserName + "] userId=[" + toUserId
//					+ "] deviceId=[" + deviceId + "]");
//
//			if (!sessionService.isOnline(toUserId, deviceId)) {
//				// LOGIC: send a offline message through apns
//				String apnsToken = gaSession.getApnsToken();
//				if (apnsToken != null && apnsToken.length() > 0) {
//					String osVersion = gaSession.getOsVersion();
//					if (osVersion == null) {
//						;
//					} else {
//						int badgeNum = badgeNumService.countBadgeNum(toUserId);
//						if (osVersion.toLowerCase().contains("ios")) {
//							GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
//									.getBean("gaApnsService");
//							apnsService.sendActivityJoin(false, apnsToken, badgeNum, activityName, memberName);
//
//							logger.info("friend session=[" + gaSession.getIoSessionId()
//									+ "] offline, to apns: username=[" + toUserName + "] userId=[" + toUserId
//									+ "] of device=[" + deviceId + "]");
//						} else { // android
//							GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context
//									.getBean("gaJPushService");
//							jpushService.sendActivityJoin(false, apnsToken, badgeNum, activityName, memberName);
//
//							logger.info("friend session=[" + gaSession.getIoSessionId()
//									+ "] offline, to JPush: username=[" + toUserName + "] userId=[" + toUserId
//									+ "] of device=[" + deviceId + "]");
//						}
//					}
//				} else {
//					logger.warn("friend session=[" + gaSession.getIoSessionId() + "] offline, and no token: username=["
//							+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
//
//					continue;
//				}
//			}
//		}
//	}

	@Override
	public void multicast(ApplicationContext context, GaApplyStateNotify notify)
			throws UnsupportedEncodingException, InterruptedException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		String toUserId = notify.getToAccountId();
		String toUserName = "";
		int currentTimestamp = DatetimeUtil.currentTimestamp();

		Set<String> deviceSet = sessionService.getDeviceList(toUserId);
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String deviceId = it.next();
			logger.debug("to deviceId: " + deviceId);

			GaSessionInfo gaSession = sessionService.getSession(deviceId);
			if (gaSession == null) {
				logger.warn("no session login after MemCached reset: username(" + toUserName + ") userId(" + toUserId
						+ ") deviceId(" + deviceId + ")");
				break;
			}
			long ioSessionId = gaSession.getIoSessionId();
			logger.debug("to session=[" + ioSessionId + "] username=[" + toUserName + "] userId=[" + toUserId
					+ "] deviceId=[" + deviceId + "]");

			String osVersion = gaSession.getOsVersion();
			if (osVersion == null) {
				;
			} else {
				String apnsToken = gaSession.getApnsToken();
				int badgeNum = badgeNumService.countBadgeNum(toUserId);

				if (osVersion.toLowerCase().contains("ios")) {
					if (sessionService.isOnline(toUserId, deviceId)) {
						// on same stp
						if (gcv.getStpId().equals(gaSession.getStpId())) {
							IoSession ioSession = this.getIoService().getManagedSessions().get(ioSessionId);
							// this session is offline now!
							if (ioSession == null) {
								sessionService.inactive(toUserId, deviceId);
								continue; // next device
							}

							List<GaApplyStateNotify> array = new ArrayList<GaApplyStateNotify>();
							array.add(notify);
							SyncApplyStateResp syncApplyResp = new SyncApplyStateResp(currentTimestamp, ErrorCode.SUCCESS, array);
							TlvObject tlvSyncApplyResp = CommandParser.encode(syncApplyResp);

							WriteFuture future = ioSession.write(tlvSyncApplyResp);
							// Wait until the message is completely written out
							// to the O/S buffer.
							future.awaitUninterruptibly();
							if (future.isWritten()) {
								// The message has been written successfully.
								logger.info("friend session=[" + ioSessionId + "] is online, username=[" + toUserName
										+ "] userId=[" + toUserId + "] deviceId=[" + deviceId
										+ "] write invite success!");
								
								for (GaApplyStateNotify data : array) {
									GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
									// TODO android testing, not changed sync state
									applyService.modifySyncStateToReceived(data.getFromAccountId(), data.getToAccountId(),
											data.getChannelId(), currentTimestamp);
								}
							} else {
								// The messsage couldn't be written out
								// completely for some reason. (e.g. Connection
								// is closed)
								logger.warn("friend session=[" + ioSessionId + "] write invite fail: username=["
										+ toUserName + "] userId=[" + toUserId + "] deviceId=[" + deviceId + "]");

								// LOGIC: clean this session from sessionMap.
								sessionService.inactive(toUserId, deviceId);
								ioSession.close(true);
							}
						} else { // on other stp
							StpServerInfoJsonBean stp = sessionService.getStp(gaSession.getStpId());
							if (stp == null) {
								logger.warn("stp=[" + gaSession.getStpId() + "] has no session=[" + ioSessionId
										+ "] as username=[" + toUserName + "] userId=[" + toUserId + "] of deviceId=["
										+ deviceId + "]");

								// LOGIC: send a offline message through apns
								if (apnsToken != null && apnsToken.length() > 0) {
									logger.info("friend session=[" + ioSessionId + "] offline, send apns: username=["
											+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");

									GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context
											.getBean("gaApnsService");
									apnsService.sendApplyState(false, apnsToken, badgeNum, notify);
								} else {
									logger.warn("friend session=[" + ioSessionId
											+ "] offline, and no token: username=[" + toUserName + "] userId=["
											+ toUserId + "] of device=[" + deviceId + "]");
								}
							} else {
								// Logic: send to MQ; another stp will send it
								// to friend. send a transmit message to another
								// stp.
								InlinecastApplyStateReq reqCmd = new InlinecastApplyStateReq(ioSessionId, notify);
								TlvObject tlvReq = reqCmd.encode();

								logger.info("frined session=[" + ioSessionId + "] is online another STP: username=["
										+ toUserName + "] userId(" + toUserId + "] of deviceId=[" + deviceId
										+ "] on STP=[" + stp.getServerIp() + ":" + stp.getPort() + "]");

								InlinecastSocketManager socket = GenericSingleton
										.getInstance(InlinecastSocketManager.class);
								socket.sendto(stp.getServerIp(), stp.getPort(), tlvReq);
							}
						}
					} else { // offline
						GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
						apnsService.sendApplyState(false, apnsToken, badgeNum, notify);

						logger.info("friend session=[" + gaSession.getIoSessionId() + "] offline, to apns: username=["
								+ toUserName + "] userId=[" + toUserId + "] of device=[" + deviceId + "]");
					}
				} else { // android
					GaOfflineNotifyService jpushService = (GaOfflineNotifyService) context.getBean("gaJPushService");
					String jpushAlias = EcryptUtil.md5ChatId(toUserId, deviceId);

					if (sessionService.isOnline(toUserId, deviceId)) {
						jpushService.sendApplyState(true, jpushAlias, badgeNum, notify);
						logger.debug("send online invite message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					} else {
						jpushService.sendApplyState(false, jpushAlias, badgeNum, notify);
						logger.debug("send offline invite message to " + toUserName + "[" + toUserId
								+ "] android device[" + deviceId + "] by jpush alias[" + jpushAlias + "]");
					}
				}
			}
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastMessageServiceImpl.class);
}
