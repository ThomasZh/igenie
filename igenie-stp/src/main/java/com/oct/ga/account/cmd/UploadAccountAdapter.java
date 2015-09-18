package com.oct.ga.account.cmd;

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
import com.oct.ga.comm.cmd.account.UploadAccountReq;
import com.oct.ga.comm.cmd.account.UploadAccountResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class UploadAccountAdapter
		extends StpReqCommand
{
	public UploadAccountAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_MY_ACCOUNT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UploadAccountReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String myAccountId = this.getMyAccountId();
		AccountMaster accountDetail = reqCmd.getAccount();

		try {
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			AccountBasic account = new AccountBasic();
			if (accountDetail.getAccountId() == null || accountDetail.getAccountId().length() == 0) {
				account.setAccountId(myAccountId);
			} else {
				account.setAccountId(accountDetail.getAccountId());
			}
			account.setNickname(accountDetail.getNickname());
			account.setAvatarUrl(accountDetail.getAvatarUrl());
			account.setDesc(accountDetail.getDesc());
			supAccountService.modifyAccountBasicInfo(account, currentTimestamp);

			followingService.updateMyLastUpdateTimeInFollowed(account.getAccountId(), currentTimestamp);

			try {
				GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

				String activityId = taskService.modifyExerciseProject2Completed(account.getAccountId(),
						GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_1, currentTimestamp);
				
				if (activityId != null) {
					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activityId);
					log.setFromAccountId(account.getAccountId());
					log.setActionTag(GlobalArgs.TASK_ACTION_COMPLETED);
					log.setToActionId(activityId);
					taskService.addLog(log, currentTimestamp);

					taskService.addLogExtend(log.getLogId(), account.getAccountId(), activityId,
							GlobalArgs.TASK_ACTION_COMPLETED, GlobalArgs.SYNC_STATE_READ, currentTimestamp);
				}
			} catch (Exception e) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|add task log error: " + LogErrorMessage.getFullInfo(e));
			}

			UploadAccountResp respCmd = new UploadAccountResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			UploadAccountResp respCmd = new UploadAccountResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private UploadAccountReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UploadAccountAdapter.class);

}
