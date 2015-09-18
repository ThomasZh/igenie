package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.auth.LogoutReq;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.session.SupSessionService;

public class LogoutAdapter
		extends StpReqCommand
{
	public LogoutAdapter()
	{
		super();

		this.setTag(Command.LOGOUT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new LogoutReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");
		supSessionService.removeStpSession(accountId);

		// session.close(true);
		logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
				+ "]|commandTag=[" + this.getTag() + "]|SessionEnd logout");

		return null;
	}

	private LogoutReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(LogoutAdapter.class);

}
