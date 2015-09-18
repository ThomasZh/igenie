package com.oct.ga.publish.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.publish.ModifyPublishLocReq;
import com.oct.ga.comm.cmd.publish.ModifyPublishLocResp;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaPublishService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ModifyPublishLocAdapter
		extends StpReqCommand
{
	public ModifyPublishLocAdapter()
	{
		super();

		this.setTag(Command.PUBLISH_MODIFY_LOC_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyPublishLocReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		List<GaPublishLoc> locations = reqCmd.getLocations();

		try {
			GaPublishService publishService = (GaPublishService) context.getBean("gaPublishService");

			publishService.modifyPublishLoc(activityId, locations, currentTimestamp);

			ModifyPublishLocResp respCmd = new ModifyPublishLocResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ModifyPublishLocResp respCmd = new ModifyPublishLocResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyPublishLocReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyPublishLocAdapter.class);

}
