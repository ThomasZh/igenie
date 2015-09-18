package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.message.SupMessageService;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateReq;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateResp;

public class MultcastModifyApproveStateAdapter
		extends SupReqCommand
{
	public MultcastModifyApproveStateAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastModifyApproveStateReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getChannelId();
		String accountId = reqCmd.getToAccountId();
		short approveState = reqCmd.getApproveState();
		String txt = reqCmd.getTxt();
		String fromAccountId = reqCmd.getFromAccountId();
		String fromAccountName = reqCmd.getFromAccountName();
		String fromAccountAvatarUrl = reqCmd.getFromAccountAvatarUrl();
		int currentTimestamp = DatetimeUtil.currentTimestamp();

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			String groupName = groupService.queryGroupName(activityId);
			if (approveState == GlobalArgs.INVITE_STATE_ACCPET) {
				// Logic: follow to each other.
				followingService.follow(fromAccountId, accountId, currentTimestamp);
				followingService.follow(accountId, fromAccountId, currentTimestamp);

				groupService.acceptToJoin(activityId, accountId, currentTimestamp);

				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(activityId);
				log.setFromAccountId(fromAccountId);
				log.setActionTag(GlobalArgs.TASK_ACTION_ACCEPT);
				log.setToActionId(accountId);
				taskService.addLog(log, currentTimestamp);

				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_ACCEPT,
						syncState, currentTimestamp);
				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
				taskService.addLogExtend(log.getLogId(), fromAccountId, activityId, GlobalArgs.TASK_ACTION_ACCEPT,
						syncState, currentTimestamp);

				// TODO send notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(accountId);
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error(LogErrorMessage.getFullInfo(e));
				}
			} else if (approveState == GlobalArgs.INVITE_STATE_REJECT) {
				groupService.rejectToJoin(activityId, accountId, currentTimestamp);

				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(activityId);
				log.setFromAccountId(fromAccountId);
				log.setActionTag(GlobalArgs.TASK_ACTION_REJECT);
				log.setToActionId(accountId);
				taskService.addLog(log, currentTimestamp);

				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_REJECT,
						syncState, currentTimestamp);
				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
				taskService.addLogExtend(log.getLogId(), fromAccountId, activityId, GlobalArgs.TASK_ACTION_REJECT,
						syncState, currentTimestamp);
				
				// TODO send notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(accountId);
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error(LogErrorMessage.getFullInfo(e));
				}
			} else if (approveState == GlobalArgs.INVITE_STATE_REFILL) {
				groupService.acceptToJoin(activityId, accountId, currentTimestamp);
				groupService.modifyMemberState(activityId, accountId, approveState, currentTimestamp);

				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(activityId);
				log.setFromAccountId(fromAccountId);
				log.setActionTag(GlobalArgs.TASK_ACTION_REFILL);
				log.setToActionId(accountId);
				taskService.addLog(log, currentTimestamp);

				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_REFILL,
						syncState, currentTimestamp);
				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
				taskService.addLogExtend(log.getLogId(), fromAccountId, activityId, GlobalArgs.TASK_ACTION_REFILL,
						syncState, currentTimestamp);
				
				// TODO send notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(accountId);
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error(LogErrorMessage.getFullInfo(e));
				}
			}

			String msgId = applyService.modify(fromAccountId, accountId, activityId, approveState, txt,
					currentTimestamp);
			// send this apply message to online others
			{
				InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
						.getBean("inlinecastMessageService");

				GaApplyStateNotify applyNotify = new GaApplyStateNotify();
				applyNotify.setMsgId(msgId);
				applyNotify.setChannelId(activityId);
				applyNotify.setChannelName(groupName);
				applyNotify.setChatId(activityId);
				applyNotify.setFromAccountId(fromAccountId);
				applyNotify.setFromAccountName(fromAccountName);
				applyNotify.setFromAccountAvatarUrl(fromAccountAvatarUrl);
				applyNotify.setToAccountId(accountId);
				applyNotify.setAction(approveState);
				applyNotify.setTxt(txt);
				applyNotify.setTimestamp(currentTimestamp);

				inlinecastMessageService.multicast(context, applyNotify);
			}

			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
					fromAccountId, this.getTag());
			// if add/remove member, task info version must increase.
			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					fromAccountId, this.getTag());

			short applyNum = badgeNumService.countApplyNum(accountId);
			badgeNumService.modifyApplyNum(accountId, applyNum);

			MultcastModifyApproveStateResp respCmd = new MultcastModifyApproveStateResp(this.getSequence(),
					ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			MultcastModifyApproveStateResp respCmd = new MultcastModifyApproveStateResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MultcastModifyApproveStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastModifyApproveStateAdapter.class);

}
