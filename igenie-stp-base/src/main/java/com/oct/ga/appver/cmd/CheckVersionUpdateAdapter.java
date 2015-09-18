package com.oct.ga.appver.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.appver.CheckVersionUpdateReq;
import com.oct.ga.comm.cmd.appver.CheckVersionUpdateResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaAppVersionService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class CheckVersionUpdateAdapter
		extends StpReqCommand
{
	public CheckVersionUpdateAdapter()
	{
		super();

		this.setTag(Command.CHECK_VERSION_UPGRADE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new CheckVersionUpdateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String clientVersion = reqCmd.getClientVersion();

		try {
			GaAppVersionService appVersionService = (GaAppVersionService) context.getBean("gaAppVersionService");

			short priority = appVersionService.queryUpgradePriority(clientVersion);

			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|user=[" + this.getMyAccountName()
					+ "]|clientVersion=[" + clientVersion + "]|upgradePriority=[" + priority + "]");

			CheckVersionUpdateResp respCmd = new CheckVersionUpdateResp(sequence, priority);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			CheckVersionUpdateResp respCmd = new CheckVersionUpdateResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private CheckVersionUpdateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(CheckVersionUpdateAdapter.class);

}
