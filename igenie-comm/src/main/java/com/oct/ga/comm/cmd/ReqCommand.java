package com.oct.ga.comm.cmd;

import com.oct.ga.comm.DatetimeUtil;

/**
 * command with service, can do real business logic in execute method
 * 
 * @author liwenzhi
 * 
 */
public abstract class ReqCommand
		extends StpCommand
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3797008354004406939L;
	protected int currentTimestamp;

	public ReqCommand()
	{
		currentTimestamp = DatetimeUtil.currentTimestamp();
		this.setSequence(currentTimestamp);
	}

	/**
	 * check myAccountId first, than in session; may by set myAccountId in sub
	 * class, base event handler use this method to check request validity
	 * 
	 * @return current connection account
	 */
	public String getMyAccountId()
	{
		// SessionService3MapImpl sessionService =
		// GenericSingleton.getInstance(SessionService3MapImpl.class);
		// return sessionService.getAccountId(this.getMyDeviceId());
		return null;
	}

	// public void setMyAccountId(String accountId)
	// {
	// SessionService3MapImpl sessionService =
	// GenericSingleton.getInstance(SessionService3MapImpl.class);
	// sessionService.setAccountId(this.getDeviceId(), accountId);
	// }

	public String getMyAccountName()
	{
		// SessionService3MapImpl sessionService =
		// GenericSingleton.getInstance(SessionService3MapImpl.class);
		// return sessionService.getAccountName(this.getMyDeviceId());
		return null;
	}

	// public void setMyAccountName(String accountName)
	// {
	// SessionService3MapImpl sessionService =
	// GenericSingleton.getInstance(SessionService3MapImpl.class);
	// sessionService.setAccountName(this.getDeviceId(), accountName);
	// }

	public String getMyDeviceId()
	{
		return null;
	}

	public void setMyDeviceId(String deviceId)
	{
	}

	public int getCurrentTimestamp()
	{
		return currentTimestamp;
	}

	public void setCurrentTimestamp(int currentTimestamp)
	{
		this.currentTimestamp = currentTimestamp;
	}

}
