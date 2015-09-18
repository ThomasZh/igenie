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
import com.oct.ga.comm.cmd.club.ActivityUpdateSubscribersReq;
import com.oct.ga.comm.cmd.club.ActivityUpdateSubscribersResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityUpdateSubscribersAdapter
		extends StpReqCommand
{
	public ActivityUpdateSubscribersAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_UPDATE_SUBSCRIBERS_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityUpdateSubscribersReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityUpdateSubscribersResp respCmd = null;
		String activityId = reqCmd.getActivityId();
		String[] subscriberIds = reqCmd.getSubscriberIds();
		String creatorId = this.getMyAccountId();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			List<AccountBasic> oldSubscribers = activityService.querySubscribers(activityId);

			GaTaskLog log = new GaTaskLog();
			log.setLogId(UUID.randomUUID().toString());
			log.setChannelId(activityId);
			log.setFromAccountId(this.getMyAccountId());
			log.setActionTag(GlobalArgs.TASK_ACTION_ADD);
			log.setToActionId(activityId);
			taskService.addLog(log, currentTimestamp);
			
			if (subscriberIds != null && subscriberIds.length > 0) {
				// Logic: add to subscribe
				for (String id : subscriberIds) {
					if (!activityService.isExistSubscribe(activityId, id)) {
						activityService.addSubscribe(activityId, activityId, id, GlobalArgs.SYNC_STATE_NOT_RECEIVED,
								currentTimestamp);
						
						short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
						taskService.addLogExtend(log.getLogId(), id, activityId, GlobalArgs.TASK_ACTION_ADD, syncState,
								currentTimestamp);
					}
				}

				// Logic: remove subscribe
				for (AccountBasic oldSubscriber : oldSubscribers) {
					String oldSubscriberId = oldSubscriber.getAccountId();
					int count = 0;

					for (String id : subscriberIds) {
						if (id.equals(oldSubscriberId)) {
							count++;
							break;
						}
					}

					if (count == 0) { // not exist
						if (!oldSubscriberId.equals(creatorId)) {
							activityService.kickoutSubscriber(activityId, oldSubscriberId, currentTimestamp);
						}
					}
				}
			}

			respCmd = new ActivityUpdateSubscribersResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityUpdateSubscribersResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActivityUpdateSubscribersReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityUpdateSubscribersAdapter.class);

}
