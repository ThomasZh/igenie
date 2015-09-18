package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.mina.core.service.IoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.ModifyApproveStateReq;
import com.oct.ga.comm.cmd.apply.ModifyApproveStateResp;
import com.oct.ga.comm.domain.account.AccountBasic;
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
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.message.SupMessageService;

public class ModifyApproveStateAdapter
		extends StpReqCommand
{
	public ModifyApproveStateAdapter()
	{
		super();

		this.setTag(Command.MODIFY_APPROVE_STATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyApproveStateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		String accountId = reqCmd.getAccountId();
		short approveState = reqCmd.getApproveState();
		String txt = reqCmd.getTxt();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");

		try {
			SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");
			
			supMessageService.sendModifyApplyState(activityId, approveState, txt, fromAccountId, fromAccountName,
					fromAccountAvatarUrl, accountId, currentTimestamp);
			
			logger.debug("send multcast modify apply state from=[" + fromAccountId + "]");
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ModifyApproveStateResp respCmd = new ModifyApproveStateResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}

		ModifyApproveStateResp respCmd = new ModifyApproveStateResp(sequence, ErrorCode.SUCCESS);
		return respCmd;

//		try {
//			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
//			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
//			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
//			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
//			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
//			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
//
//			if (approveState == GlobalArgs.INVITE_STATE_ACCPET) {
//				// Logic: follow to each other.
//				followingService.follow(this.getMyAccountId(), accountId, currentTimestamp);
//				followingService.follow(accountId, this.getMyAccountId(), currentTimestamp);
//
//				groupService.acceptToJoin(activityId, accountId, currentTimestamp);
//
//				GaTaskLog log = new GaTaskLog();
//				log.setLogId(UUID.randomUUID().toString());
//				log.setChannelId(activityId);
//				log.setFromAccountId(this.getMyAccountId());
//				log.setActionTag(GlobalArgs.TASK_ACTION_ACCEPT);
//				log.setToActionId(accountId);
//				taskService.addLog(log, currentTimestamp);
//
//				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_ACCEPT,
//						syncState, currentTimestamp);
//				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
//						GlobalArgs.TASK_ACTION_ACCEPT, syncState, currentTimestamp);
//			} else if (approveState == GlobalArgs.INVITE_STATE_REJECT) {
//				groupService.rejectToJoin(activityId, accountId, currentTimestamp);
//
//				GaTaskLog log = new GaTaskLog();
//				log.setLogId(UUID.randomUUID().toString());
//				log.setChannelId(activityId);
//				log.setFromAccountId(this.getMyAccountId());
//				log.setActionTag(GlobalArgs.TASK_ACTION_REJECT);
//				log.setToActionId(accountId);
//				taskService.addLog(log, currentTimestamp);
//
//				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_REJECT,
//						syncState, currentTimestamp);
//				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
//						GlobalArgs.TASK_ACTION_REJECT, syncState, currentTimestamp);
//			} else if (approveState == GlobalArgs.INVITE_STATE_REFILL) {
//				groupService.acceptToJoin(activityId, accountId, currentTimestamp);
//				groupService.modifyMemberState(activityId, accountId, approveState, currentTimestamp);
//
//				GaTaskLog log = new GaTaskLog();
//				log.setLogId(UUID.randomUUID().toString());
//				log.setChannelId(activityId);
//				log.setFromAccountId(this.getMyAccountId());
//				log.setActionTag(GlobalArgs.TASK_ACTION_REFILL);
//				log.setToActionId(accountId);
//				taskService.addLog(log, currentTimestamp);
//
//				short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), accountId, activityId, GlobalArgs.TASK_ACTION_REFILL,
//						syncState, currentTimestamp);
//				syncState = GlobalArgs.SYNC_STATE_RECEIVED;
//				taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
//						GlobalArgs.TASK_ACTION_REFILL, syncState, currentTimestamp);
//			}
//
//			String msgId = applyService.modify(this.getMyAccountId(), accountId, activityId, approveState, txt,
//					currentTimestamp);
//			// send this apply message to online others
//			{
//				SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
//				InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
//						.getBean("inlinecastMessageService");
//				IoService ioService = session.getService();
//				inlinecastMessageService.setIoService(ioService);
//
//				GaApplyStateNotify applyNotify = new GaApplyStateNotify();
//				applyNotify.setMsgId(msgId);
//				applyNotify.setChannelId(activityId);
//				String groupName = groupService.queryGroupName(activityId);
//				applyNotify.setChannelName(groupName);
//				applyNotify.setChatId(activityId);
//				applyNotify.setFromAccountId(this.getMyAccountId());
//				AccountBasic fromAccount = accountService.queryAccount(this.getMyAccountId());
//				applyNotify.setFromAccountName(fromAccount.getNickname());
//				applyNotify.setFromAccountAvatarUrl(fromAccount.getAvatarUrl());
//				applyNotify.setToAccountId(accountId);
//				applyNotify.setAction(approveState);
//				applyNotify.setTxt(txt);
//				applyNotify.setTimestamp(currentTimestamp);
//
//				inlinecastMessageService.multicast(context, applyNotify);
//			}
//
//			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
//					this.getMyAccountId(), this.getTag());
//			// if add/remove member, task info version must increase.
//			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
//					this.getMyAccountId(), this.getTag());
//
//			short applyNum = badgeNumService.countApplyNum(accountId);
//			badgeNumService.modifyApplyNum(accountId, applyNum);
//
//			ModifyApproveStateResp respCmd = new ModifyApproveStateResp(sequence, ErrorCode.SUCCESS);
//			return respCmd;
//		} catch (Exception e) {
//			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
//					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
//					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
//
//			ModifyApproveStateResp respCmd = new ModifyApproveStateResp(sequence, ErrorCode.UNKNOWN_FAILURE);
//			return respCmd;
//		}
	}

	private ModifyApproveStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyApproveStateAdapter.class);

}
