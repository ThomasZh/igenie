package com.oct.ga.stp.cmd;

import org.apache.mina.core.session.IoSession;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.RespCommand;

/**
 * command with service, can do real business logic in execute method
 * 
 * @author liwenzhi
 * 
 */
public class StpReqCommand
		extends ReqCommand
{
	protected IoSession session;

	public StpReqCommand()
	{
		super();
	}

	/**
	 * check myAccountId first, than in session; may by set myAccountId in sub
	 * class, base event handler use this method to check request validity
	 * 
	 * @return current connection account
	 */
	public String getMyAccountId()
	{
		return (String) session.getAttribute("accountId");
	}

	public String getMyAccountName()
	{
		return (String) session.getAttribute("accountName");
	}

	public String getMyDeviceId()
	{
		return (String) session.getAttribute("deviceId");
	}

	public void setMyDeviceId(String deviceId)
	{
		session.setAttribute("deviceId", deviceId);
	}

	public IoSession getSession()
	{
		return session;
	}

	public void setSession(IoSession session)
	{
		this.session = session;
	}

	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		return null;
	}

}
