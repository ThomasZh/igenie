package com.oct.ga.group.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.group.DndSetReq;
import com.oct.ga.comm.cmd.group.DndSetResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class DndSetAdapter
		extends StpReqCommand
{
	public DndSetAdapter()
	{
		super();

		this.setTag(Command.DND_SET_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new DndSetReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		DndSetResp respCmd = null;
		short mode = reqCmd.getMode();
		String groupId = reqCmd.getGroupId();

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			groupService.setDndMode(groupId, getMyAccountId(), mode, currentTimestamp);

			respCmd = new DndSetResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new DndSetResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private DndSetReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(DndSetAdapter.class);

}
