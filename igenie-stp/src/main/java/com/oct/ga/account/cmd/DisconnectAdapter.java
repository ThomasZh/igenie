package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.session.SupSessionService;

public class DisconnectAdapter
		extends StpReqCommand
{
	public DisconnectAdapter()
	{
		super();

		this.setTag(Command.DISCONNECT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.DISCONNECT_REQ + ", child=0) to command");

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");
		supSessionService.inactiveStpSession(accountId);

		session.close(true);
		logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
				+ "]|commandTag=[" + this.getTag() + "]|SessionEnd disconnect");

		return null;
	}

	private final static Logger logger = LoggerFactory.getLogger(DisconnectAdapter.class);

}
