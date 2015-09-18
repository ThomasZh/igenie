package com.oct.ga.desc.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.desc.ActivityCreateDescReq;
import com.oct.ga.comm.cmd.desc.ActivityCreateDescResp;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaDescService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityCreateDescAdapter
		extends StpReqCommand
{
	public ActivityCreateDescAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_CREATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityCreateDescReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		GaDescChapter chapter = reqCmd.getChapter();

		try {
			GaDescService descService = (GaDescService) context.getBean("gaDescService");

			descService.modify(activityId, chapter.getSeq(), chapter, currentTimestamp);

			ActivityCreateDescResp respCmd = new ActivityCreateDescResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ActivityCreateDescResp respCmd = new ActivityCreateDescResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActivityCreateDescReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityCreateDescAdapter.class);

}
