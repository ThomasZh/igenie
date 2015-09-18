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
import com.oct.ga.comm.cmd.desc.ActivityModifyAllDescReq;
import com.oct.ga.comm.cmd.desc.ActivityModifyAllDescResp;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaDescService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityModifyAllDescAdapter
		extends StpReqCommand
{
	public ActivityModifyAllDescAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_MODIFY_ALL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityModifyAllDescReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		List<GaDescChapter> chapters = reqCmd.getDescChapters();

		try {
			GaDescService descService = (GaDescService) context.getBean("gaDescService");

			descService.removeAll(activityId);
			for (GaDescChapter chapter : chapters) {
				descService.modify(activityId, chapter.getSeq(), chapter, currentTimestamp);
			}

			ActivityModifyAllDescResp respCmd = new ActivityModifyAllDescResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ActivityModifyAllDescResp respCmd = new ActivityModifyAllDescResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ActivityModifyAllDescReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityModifyAllDescAdapter.class);

}
