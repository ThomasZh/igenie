package com.redoct.ga.sup.admin.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.admin.ModifySupServerStateReq;
import com.oct.ga.comm.cmd.admin.ModifySupServerStateResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

public class ModifySupServerStateAdapter
		extends StpReqCommand
{
	public ModifySupServerStateAdapter()
	{
		super();

		this.setTag(Command.MODIFY_SUP_STATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifySupServerStateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ModifySupServerStateResp respCmd = null;
		String supId = reqCmd.getSupId();
		short state = reqCmd.getState();

		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			socketConnectionManager.setState(supId, state);

			if (state == GlobalArgs.TRUE) {
				logger.info("set sup server (" + supId + ") active");
			} else {
				logger.info("set sup server (" + supId + ") inactive");
			}

			respCmd = new ModifySupServerStateResp(sequence, ErrorCode.SUCCESS);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ModifySupServerStateResp(sequence, ErrorCode.UNKNOWN_FAILURE);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private ModifySupServerStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifySupServerStateAdapter.class);

}
