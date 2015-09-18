package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityRecommendReq;
import com.oct.ga.comm.cmd.club.ActivityRecommendResp;
import com.oct.ga.comm.domain.club.ActivityRecommend;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class ActivityRecommendAdapter
		extends StpReqCommand
{
	public ActivityRecommendAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_RECOMMEND_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityRecommendReq().decode(tlv);
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
		ActivityRecommendResp respCmd = null;
		ActivityRecommend recommend = reqCmd.getRecommend();
		String activityId = recommend.getActivityId();
		String[] toAccountIds = recommend.getToUserIds();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			recommend.setFromUserId(this.getMyAccountId());
			recommend.setFromUserName(this.getMyAccountName());
			recommend.setTimestamp(currentTimestamp);
			recommend.setSyncState(GlobalArgs.SYNC_STATE_NOT_RECEIVED);

			activityService.create(recommend);

			GaTaskLog log = new GaTaskLog();
			log.setLogId(UUID.randomUUID().toString());
			log.setChannelId(activityId);
			log.setFromAccountId(this.getMyAccountId());
			log.setActionTag(GlobalArgs.TASK_ACTION_RECOMMEND);
			log.setToActionId(activityId);
			taskService.addLog(log, currentTimestamp);

			String groupName = groupService.queryGroupName(activityId);

			for (String toAccountId : toAccountIds) {
				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				taskService.addLogExtend(log.getLogId(), toAccountId, activityId, GlobalArgs.TASK_ACTION_RECOMMEND,
						syncState, currentTimestamp);

				// TODO send notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(toAccountId);
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					logger.debug("send task log notify message to=" + toAccountId);
					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send task log notify message error: "
							+ LogErrorMessage.getFullInfo(e));
				}
			}

			respCmd = new ActivityRecommendResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityRecommendResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityRecommendReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityRecommendAdapter.class);

}
