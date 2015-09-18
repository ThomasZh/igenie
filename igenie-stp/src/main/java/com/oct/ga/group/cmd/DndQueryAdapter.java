package com.oct.ga.group.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.group.DndQueryReq;
import com.oct.ga.comm.cmd.group.DndQueryResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class DndQueryAdapter
		extends StpReqCommand
{
	public DndQueryAdapter()
	{
		super();

		this.setTag(Command.DND_QUERY_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new DndQueryReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		DndQueryResp respCmd = null;
		String groupId = reqCmd.getGroupId();
		short mode = GlobalArgs.DND_NO;

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			mode = groupService.queryDndMode(groupId, getMyAccountId());

			respCmd = new DndQueryResp(sequence, ErrorCode.SUCCESS, mode);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new DndQueryResp(sequence, ErrorCode.UNKNOWN_FAILURE, mode);
			return respCmd;
		}
	}

	private DndQueryReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(DndQueryAdapter.class);

}
