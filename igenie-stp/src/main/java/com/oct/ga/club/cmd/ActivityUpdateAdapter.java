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
import com.oct.ga.comm.cmd.club.ActivityUpdateReq;
import com.oct.ga.comm.cmd.club.ActivityUpdateResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivityUpdateInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityUpdateAdapter
		extends StpReqCommand
{
	public ActivityUpdateAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_UPDATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityUpdateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityUpdateResp respCmd = null;
		ActivityUpdateInfo activity = reqCmd.getActivity();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			// Logic: check time is outof today
			if (activity.getStartTime() < currentTimestamp) {
				logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.ACTIVITY_ALREADY_STARTED + "]|activity already started, can't modify.");

				respCmd = new ActivityUpdateResp(ErrorCode.ACTIVITY_ALREADY_STARTED, activity.getId());
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				TaskProExtInfo task = taskService.query(activity.getId());

				activityService.update(activity, this.getMyAccountId(), currentTimestamp);
				groupService.modifyGroupName(activity.getId(), activity.getName(), currentTimestamp);
				syncVerService.increase(activity.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
						this.getMyAccountId(), this.getTag());

				if (task.getStartTime() != activity.getStartTime() || task.getEndTime() != activity.getEndTime()) {
					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activity.getId());
					log.setFromAccountId(this.getMyAccountId());
					log.setActionTag(GlobalArgs.TASK_ACTION_CHANGE_TIME);
					log.setToActionId(activity.getId());
					taskService.addLog(log, currentTimestamp);

					List<AccountBasic> subscribers = activityService.querySubscribers(activity.getId());
					for (AccountBasic subscriber : subscribers) {
						short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
						// do not notify sender.
						if (this.getMyAccountId().equals(subscriber.getAccountId())) {
							syncState = GlobalArgs.SYNC_STATE_RECEIVED;
						}
						taskService.addLogExtend(log.getLogId(), subscriber.getAccountId(), activity.getId(),
								GlobalArgs.TASK_ACTION_CHANGE_TIME, syncState, currentTimestamp);
					}
				}

			}

			respCmd = new ActivityUpdateResp(ErrorCode.SUCCESS, activity.getId());
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityUpdateResp(ErrorCode.UNKNOWN_FAILURE, activity.getId());
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityUpdateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityUpdateAdapter.class);

}
