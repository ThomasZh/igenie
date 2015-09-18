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
import com.oct.ga.comm.cmd.club.KickoutMemberReq;
import com.oct.ga.comm.cmd.club.KickoutMemberResp;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class KickoutMemberAdapter
		extends StpReqCommand
{
	public KickoutMemberAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_KICKOUT_MEMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new KickoutMemberReq().decode(tlv);
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
		KickoutMemberResp respCmd = null;
		String activityId = reqCmd.getActivityId();
		String memberId = reqCmd.getMemberId();
		String myAccountId = this.getMyAccountId();

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			if (groupService.isActive(activityId)) {
				String leaderId = groupService.queryLeaderId(activityId);

				if (leaderId.equals(myAccountId)) {
					if (leaderId.equals(memberId)) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|activity=[" + activityId + "]|kickout leader not allow.");

						respCmd = new KickoutMemberResp(sequence, ErrorCode.NOT_ALLOW);
						return respCmd;
					}
				} else {
					if (!myAccountId.equals(memberId)) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|activity=[" + activityId + "]|kickout member(" + memberId
								+ ") not allow, you are not leader.");

						respCmd = new KickoutMemberResp(sequence, ErrorCode.NOT_ALLOW);
						return respCmd;
					}
				}

				groupService.kickout(activityId, memberId, currentTimestamp, myAccountId);
				List<String> childTaskIds = taskService.queryTaskIdsByProject(activityId);
				for (String childTaskId : childTaskIds) {
					if (groupService.isLeader(childTaskId, memberId)) {
						groupService.joinAsLeader(childTaskId, leaderId, currentTimestamp);
					}
					groupService.kickout(childTaskId, memberId, currentTimestamp, myAccountId);

					syncVerService.increase(childTaskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
							myAccountId, this.getTag());
					// if add/remove member, task info version must increase.
					syncVerService.increase(childTaskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
							myAccountId, this.getTag());
				}

				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
						myAccountId, this.getTag());
				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
						myAccountId, this.getTag());
				// if add/remove member, task info version must increase.
				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
						myAccountId, this.getTag());

				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(activityId);
				log.setFromAccountId(this.getMyAccountId());
				short actionTag = GlobalArgs.TASK_ACTION_KICKOUT_MEMBER;
				if (memberId.equals(this.getMyAccountId())) {
					actionTag = GlobalArgs.TASK_ACTION_QUIT;
				}
				log.setActionTag(actionTag);
				log.setToActionId(memberId);
				taskService.addLog(log, currentTimestamp);

				short syncState = GlobalArgs.SYNC_STATE_RECEIVED;
				taskService.addLogExtend(log.getLogId(), leaderId, activityId, actionTag, syncState, currentTimestamp);
				syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				taskService.addLogExtend(log.getLogId(), memberId, activityId, actionTag, syncState, currentTimestamp);

				String groupName = groupService.queryGroupName(activityId);
				// Logic send notify to leader or member
				try {
					switch (actionTag) {
					case GlobalArgs.TASK_ACTION_QUIT: { // send to leader
						MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
						msgFlowBasicInfo.setLogId(log.getLogId());
						msgFlowBasicInfo.setFromAccountId(fromAccountId);
						msgFlowBasicInfo.setFromAccountName(fromAccountName);
						msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
						msgFlowBasicInfo.setToActionAccountId(leaderId);
						msgFlowBasicInfo.setToActionId(log.getChannelId());
						msgFlowBasicInfo.setActionTag(log.getActionTag());
						msgFlowBasicInfo.setChannelId(log.getChannelId());
						msgFlowBasicInfo.setChannelName(groupName);

						SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

						supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
					}
						break;
					case GlobalArgs.TASK_ACTION_KICKOUT_MEMBER: { // send to member
						MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
						msgFlowBasicInfo.setLogId(log.getLogId());
						msgFlowBasicInfo.setFromAccountId(fromAccountId);
						msgFlowBasicInfo.setFromAccountName(fromAccountName);
						msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
						msgFlowBasicInfo.setToActionAccountId(memberId);
						msgFlowBasicInfo.setToActionId(log.getChannelId());
						msgFlowBasicInfo.setActionTag(log.getActionTag());
						msgFlowBasicInfo.setChannelId(log.getChannelId());
						msgFlowBasicInfo.setChannelName(groupName);

						SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

						supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
					}
						break;
					}
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send task log notify message error: "
							+ LogErrorMessage.getFullInfo(e));
				}

				// TODO badge number
				short taskLogNum = badgeNumService.queryTaskLogNum(memberId);
				badgeNumService.modifyTaskLogNum(memberId, ++taskLogNum);

				logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|activity=[" + activityId
						+ "]|success.");

				respCmd = new KickoutMemberResp(sequence, ErrorCode.SUCCESS);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.ACTIVITY_ALREADY_CANCELED + "]|activity=[" + activityId + "] already canceled.");

				respCmd = new KickoutMemberResp(sequence, ErrorCode.ACTIVITY_ALREADY_CANCELED);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new KickoutMemberResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private KickoutMemberReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(KickoutMemberAdapter.class);

}
