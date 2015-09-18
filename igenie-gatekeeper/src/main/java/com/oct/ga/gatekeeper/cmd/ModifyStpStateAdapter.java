package com.oct.ga.gatekeeper.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.gatekeeper.ModifyStpStateReq;
import com.oct.ga.comm.cmd.gatekeeper.ModifyStpStateResp;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.gatekeeper.ServerListCache;
import com.oct.ga.stp.cmd.StpReqCommand;

// serverIp,port,sessionNumber
public class ModifyStpStateAdapter
		extends StpReqCommand
{
	public ModifyStpStateAdapter()
	{
		super();

		this.setTag(Command.GK_MODIFY_STP_STATE_REQ);
	}

	@Override
	public StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyStpStateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ModifyStpStateResp respCmd = null;
		String stpId = reqCmd.getStpId();
		short state = reqCmd.getState();

		try {
			ServerListCache serverList = GenericSingleton.getInstance(ServerListCache.class);
			serverList.setState(stpId, state);
			
			StpServerInfoJsonBean serverInfo = serverList.get(stpId);
			if (state == ErrorCode.SUCCESS) {
				logger.info("set stp server (" + serverInfo.getServerIp() + ":" + serverInfo.getPort() + ") active");
			} else {
				logger.info("set stp server (" + serverInfo.getServerIp() + ":" + serverInfo.getPort() + ") inactive");
			}

			respCmd = new ModifyStpStateResp(ErrorCode.SUCCESS);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyStpStateResp(ErrorCode.UNKNOWN_FAILURE);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private ModifyStpStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyStpStateAdapter.class);
}
