package com.oct.ga.desc.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.desc.ActivityModifyDescReq;
import com.oct.ga.comm.cmd.desc.ActivityModifyDescResp;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaDescService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityModifyDescAdapter
		extends StpReqCommand
{
	public ActivityModifyDescAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_MODIFY_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityModifyDescReq().decode(tlv);
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

			ActivityModifyDescResp respCmd = new ActivityModifyDescResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ActivityModifyDescResp respCmd = new ActivityModifyDescResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActivityModifyDescReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityModifyDescAdapter.class);

}
