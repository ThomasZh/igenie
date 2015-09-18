package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class ActivityQuerySubscriberAdapter
		extends StpReqCommand
{
	public ActivityQuerySubscriberAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBERS_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQuerySubscribersReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQuerySubscribersResp respCmd = null;
		String activityId = reqCmd.getActivityId();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<AccountBasic> subscribers = activityService.querySubscribers(activityId);

			for (AccountBasic subscriber : subscribers) {
				AccountBasic account = accountService.queryAccount(subscriber.getAccountId());
				subscriber.setNickname(account.getNickname());
				subscriber.setAvatarUrl(account.getAvatarUrl());
			}

			respCmd = new ActivityQuerySubscribersResp(ErrorCode.SUCCESS, subscribers);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQuerySubscribersResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQuerySubscribersReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQuerySubscriberAdapter.class);

}
