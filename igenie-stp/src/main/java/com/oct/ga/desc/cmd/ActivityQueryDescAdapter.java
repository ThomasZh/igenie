package com.oct.ga.desc.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.desc.ActivityQueryDescReq;
import com.oct.ga.comm.cmd.desc.ActivityQueryDescResp;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaDescService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryDescAdapter
		extends StpReqCommand
{
	public ActivityQueryDescAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_QUERY_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryDescReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		ActivityQueryDescResp respCmd = null;

		try {
			GaDescService descService = (GaDescService) context.getBean("gaDescService");

			List<GaDescChapter> array = descService.query(activityId);

			respCmd = new ActivityQueryDescResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryDescResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private ActivityQueryDescReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryDescAdapter.class);

}
