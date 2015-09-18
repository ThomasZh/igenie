package com.oct.ga.task;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskNotifyReq;
import com.oct.ga.comm.cmd.task.SyncTaskActivityResp;
import com.oct.ga.comm.cmd.task.SyncTaskInfoResp;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.inlinecast.InlinecastSocketHandler;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaOfflineNotifyService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.oct.ga.session.GaSessionInfo;
import com.oct.ga.session.SessionService3MapImpl;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;

// Unicast,Multicast,Broadcast
public class InlinecastTasklogServiceImpl
		extends InlinecastMessageServiceIf
{
	public void multicast(ApplicationContext context, TaskProExtInfo taskInfo, NotifyTaskLog activity)
			throws UnsupportedEncodingException, InterruptedException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		TaskServiceImpl taskService = (TaskServiceImpl) context.getBean("gaTaskService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
		int onlineCount = 0;

		Set<String> deviceSet = sessionService.getDeviceList(activity.getSendToAccountId());
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String deviceId = it.next();
			GaSessionInfo gaSession = sessionService.getSession(deviceId);
			if (gaSession == null) {
				logger.warn("no login after MC reset: username(" + activity.getSendToAccountName() + ") userId("
						+ activity.getSendToAccountId() + ") deviceId(" + deviceId + ")");
				break;
			}

			if (sessionService.isOnline(activity.getSendToAccountId(), deviceId)) {
				onlineCount++;

				// on same stp
				if (gaSession.getStpId().equals(gcv.getStpId())) {
					long ioSessionId = gaSession.getIoSessionId();
					IoSession ioSession = ioService.getManagedSessions().get(ioSessionId);
					if (ioSession == null) { // this session is not online now!
						onlineCount--;
						sessionService.inactive(activity.getSendToAccountId(), deviceId);
						continue;
					}

					SyncTaskActivityResp activityResp = new SyncTaskActivityResp(activity);
					TlvObject tActivity = CommandParser.encode(activityResp);

					WriteFuture future = ioSession.write(tActivity);
					// Wait until the message is completely written out to the
					// O/S buffer.
					future.awaitUninterruptibly();
					if (future.isWritten()) {
						// The message has been written successfully.
						logger.info("online to send: username(" + activity.getSendToAccountName() + ") userId("
								+ activity.getSendToAccountId() + ") of deviceId(" + deviceId + ")");

						taskService.updateActivityToReadState(activity);

						if (activity.getActivityState() == GlobalArgs.INVITE_STATE_KICKOFF
								&& activity.getToAccountId().equals(activity.getSendToAccountId())) {
							// send task member kickoff.
						} else {
							// send task info;
							SyncTaskInfoResp syncTaskInfoResp = new SyncTaskInfoResp(taskInfo);
							TlvObject tSyncTaskInfo = CommandParser.encode(syncTaskInfoResp);
							ioSession.write(tSyncTaskInfo);
						}
					} else {
						// The messsage couldn't be written out completely for
						// some
						// reason.
						// (e.g. Connection is closed)
						logger.warn("online to send fail: username(" + activity.getSendToAccountName() + ") userId("
								+ activity.getSendToAccountId() + ") of deviceId(" + deviceId + ")");

						// LOGIC: clean this session from sessionMap.
						onlineCount--;
						sessionService.inactive(activity.getSendToAccountId(), deviceId);
						ioSession.close(true);
					}
				} else { // not same stp
					StpServerInfoJsonBean stp = sessionService.getStp(gaSession.getStpId());

					// Logic: send to MQ; another stp will send it to friend.
					// send a transmit message to another stp.
					activity.setReciverIoSessionId(gaSession.getIoSessionId());
					InlinecastTaskNotifyReq reqCmd = new InlinecastTaskNotifyReq(activity);
					TlvObject tlvReq = reqCmd.encode();

					logger.info("online to another STP: username(" + activity.getSendToAccountName() + ") userId("
							+ activity.getSendToAccountId() + ") of deviceId(" + deviceId + ") on STP("
							+ stp.getServerIp() + ":" + stp.getPort() + ")");

					InlinecastSocketHandler socketHandler = new InlinecastSocketHandler();
					socketHandler.sendTo(stp.getServerIp(), stp.getPort(), tlvReq);
				}
			}
		}

		// all device offline
		if (onlineCount == 0) {
			it = deviceSet.iterator();
			while (it.hasNext()) {
				String deviceId = it.next();
				GaSessionInfo gaSession = sessionService.getSession(deviceId);
				if (gaSession == null) {
					logger.warn("no login after MC reset: username(" + activity.getToAccountName() + ") userId("
							+ activity.getToAccountId() + ") deviceId(" + deviceId + ")");
					break;
				}

				if (taskInfo.getState() != 1) // task complete
					return;

				if (!sessionService.isOnline(activity.getSendToAccountId(), deviceId)) {
					// LOGIC: send a offline message through apns
					String apnsToken = gaSession.getApnsToken();
					if (apnsToken != null && apnsToken.length() > 0) {
						logger.info("offline to apns: username(" + activity.getSendToAccountName() + ") userId("
								+ activity.getSendToAccountId() + ") of device(" + deviceId + ")");

						GaOfflineNotifyService apnsService = (GaOfflineNotifyService) context.getBean("gaApnsService");
						int badgeNum = badgeNumService.countBadgeNum(activity.getSendToAccountId());
						apnsService.sendTaskLog(false, apnsToken, badgeNum, activity);
					} else {
						logger.warn("offline no token: username(" + activity.getSendToAccountName() + ") userId("
								+ activity.getSendToAccountId() + ") of device(" + deviceId + ")");

						continue;
					}
				}
			}
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastTasklogServiceImpl.class);
}
