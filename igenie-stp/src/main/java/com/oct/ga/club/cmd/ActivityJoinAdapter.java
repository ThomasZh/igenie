package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityJoinReq;
import com.oct.ga.comm.cmd.club.ActivityJoinResp;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class ActivityJoinAdapter
		extends StpReqCommand
{
	public ActivityJoinAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_JOIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityJoinReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityJoinResp respCmd = null;
		String activityId = reqCmd.getActivityId();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");
		List<GaApplicantInfo> applicantInfos = reqCmd.getApplicantInfos();
		List<GaApplicantCell> contactInfo = reqCmd.getContactInfo();
		short action = GlobalArgs.INVITE_STATE_APPLY;
		String msgId = null;

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			if (groupService.isActive(activityId)) {
				String leaderId = groupService.queryLeaderId(activityId);

				// invite log
				if (activityService.queryApproveType(activityId) == GlobalArgs.TRUE) {
					groupService.applyWaitJoin(activityId, fromAccountId, currentTimestamp, leaderId);
					action = GlobalArgs.INVITE_STATE_APPLY;
					msgId = applyService.modify(fromAccountId, leaderId, activityId, action, null, currentTimestamp);

					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activityId);
					log.setFromAccountId(this.getMyAccountId());
					log.setActionTag(GlobalArgs.TASK_ACTION_APPLY);
					log.setToActionId(activityId);
					taskService.addLog(log, currentTimestamp);

					short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
					taskService.addLogExtend(log.getLogId(), leaderId, activityId, GlobalArgs.TASK_ACTION_APPLY,
							syncState, currentTimestamp);
					syncState = GlobalArgs.SYNC_STATE_RECEIVED;
					taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
							GlobalArgs.TASK_ACTION_APPLY, syncState, currentTimestamp);

					String groupName = groupService.queryGroupName(activityId);
					// send this apply message to online leader
					try {
						SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

//						GaApplyStateNotify applyNotify = new GaApplyStateNotify();
//						applyNotify.setMsgId(msgId);
//						applyNotify.setChannelId(activityId);
//						String groupName = groupService.queryGroupName(activityId);
//						applyNotify.setChannelName(groupName);
//						applyNotify.setChatId(activityId);
//						applyNotify.setFromAccountId(fromAccountId);
//						applyNotify.setFromAccountName(fromAccountName);
//						applyNotify.setFromAccountAvatarUrl(fromAccountAvatarUrl);
//						applyNotify.setToAccountId(leaderId);
//						applyNotify.setAction(action);
//						applyNotify.setTimestamp(currentTimestamp);
//
//						supMessageService.sendApply(applyNotify, currentTimestamp);
						
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

						supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
					} catch (Exception e) {
						logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send apply notify message error: "
								+ LogErrorMessage.getFullInfo(e));
					}
				} else { // join
					groupService.joinAsMember(activityId, this.getMyAccountId(), currentTimestamp);
					action = GlobalArgs.INVITE_STATE_JOIN;
					msgId = applyService.modify(fromAccountId, leaderId, activityId, action, null, currentTimestamp);

					// Logic: follow to each other.
					followingService.follow(leaderId, fromAccountId, currentTimestamp);
					followingService.follow(fromAccountId, leaderId, currentTimestamp);

					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activityId);
					log.setFromAccountId(this.getMyAccountId());
					log.setActionTag(GlobalArgs.TASK_ACTION_JOIN);
					log.setToActionId(activityId);
					taskService.addLog(log, currentTimestamp);

					short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
					taskService.addLogExtend(log.getLogId(), leaderId, activityId, GlobalArgs.TASK_ACTION_JOIN,
							syncState, currentTimestamp);
					syncState = GlobalArgs.SYNC_STATE_RECEIVED;
					taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
							GlobalArgs.TASK_ACTION_JOIN, syncState, currentTimestamp);

					// send this join message to online leader
					try {
						SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

						String groupName = groupService.queryGroupName(activityId);
						//supMessageService.sendActivityJoin(groupName, leaderId, fromAccountName, currentTimestamp);
						
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

						supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
					} catch (Exception e) {
						logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send apply notify message error: "
								+ LogErrorMessage.getFullInfo(e));
					}
				}

				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
						fromAccountId, this.getTag());

				// if add/remove member, task info version must increase.
				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
						fromAccountId, this.getTag());

				short applyNum = badgeNumService.countApplyNum(leaderId);
				badgeNumService.modifyApplyNum(leaderId, applyNum);

				// Logic: add contactInfoJson & applicantInfos into db
				if (activityService.queryApplyFormType(activityId) == GlobalArgs.TRUE) {
					Gson gson = new Gson();
					String contactInfoJson = gson.toJson(contactInfo);
					applyService.modifyApplicantContact(activityId, fromAccountId, contactInfoJson, currentTimestamp);
					applyService.removeAllApplicant(activityId, fromAccountId);
					if (applicantInfos != null) {
						for (GaApplicantInfo applicantInfo : applicantInfos) {
							String json = gson.toJson(applicantInfo.getApplicant());
							logger.debug("json: " + json);
							applyService.addApplicant(activityId, fromAccountId, applicantInfo.getSeq(), json,
									currentTimestamp);
						}
					}
				}

				logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|activity=[" + activityId
						+ "]|success.");

				respCmd = new ActivityJoinResp(ErrorCode.SUCCESS);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.ACTIVITY_ALREADY_CANCELED + "]|activity=[" + activityId + "] already canceled.");

				respCmd = new ActivityJoinResp(ErrorCode.ACTIVITY_ALREADY_CANCELED);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityJoinResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityJoinReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityJoinAdapter.class);

}
