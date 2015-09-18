package com.oct.ga.desc.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.desc.ActivityRemoveDescReq;
import com.oct.ga.comm.cmd.desc.ActivityRemoveDescResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaDescService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityRemoveDescAdapter
		extends StpReqCommand
{
	public ActivityRemoveDescAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_REMOVE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityRemoveDescReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		short seq = reqCmd.getSeq();
		ActivityRemoveDescResp respCmd = null;

		try {
			GaDescService descService = (GaDescService) context.getBean("gaDescService");

			descService.remove(activityId, seq);

			respCmd = new ActivityRemoveDescResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityRemoveDescResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActivityRemoveDescReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityRemoveDescAdapter.class);

}
