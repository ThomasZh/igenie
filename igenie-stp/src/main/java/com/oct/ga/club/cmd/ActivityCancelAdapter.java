package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityCancelReq;
import com.oct.ga.comm.cmd.club.ActivityCancelResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class ActivityCancelAdapter
		extends StpReqCommand
{
	public ActivityCancelAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_CANCEL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityCancelReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");
		ActivityCancelResp respCmd = null;
		String activityId = reqCmd.getActivityId();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			groupService.updateSate(activityId, GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED, currentTimestamp);
			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			activityService.cancel(activityId, this.getMyAccountId(), currentTimestamp);

			GaTaskLog log = new GaTaskLog();
			log.setLogId(UUID.randomUUID().toString());
			log.setChannelId(activityId);
			log.setFromAccountId(this.getMyAccountId());
			log.setActionTag(GlobalArgs.TASK_ACTION_CANCELED);
			log.setToActionId(activityId);
			taskService.addLog(log, currentTimestamp);

			String groupName = groupService.queryGroupName(activityId);
			
			List<AccountBasic> subscribers = activityService.querySubscribers(activityId);
			for (AccountBasic subscriber : subscribers) {
				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				// do not notify sender.
				if (this.getMyAccountId().equals(subscriber.getAccountId())) {
					syncState = GlobalArgs.SYNC_STATE_RECEIVED;
				}
				taskService.addLogExtend(log.getLogId(), subscriber.getAccountId(), activityId,
						GlobalArgs.TASK_ACTION_CANCELED, syncState, currentTimestamp);
				
				// TODO send notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(subscriber.getAccountId());
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send task log notify message error: "
							+ LogErrorMessage.getFullInfo(e));
				}
			}

			respCmd = new ActivityCancelResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityCancelResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityCancelReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityCancelAdapter.class);

}
