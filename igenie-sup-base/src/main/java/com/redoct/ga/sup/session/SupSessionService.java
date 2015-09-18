package com.redoct.ga.sup.session;

import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.session.domain.GateSession;
import com.redoct.ga.sup.session.domain.StpSession;

public interface SupSessionService
{
	/**
	 * @return gateToken
	 */
	public String applyGateToken(GateSession gateSession)
			throws SupSocketException;

	public boolean verifyGateToken(String gateToken, String deviceId)
			throws SupSocketException;

	/**
	 * one account, one session(can't login by multiply devices at the same
	 * time)
	 */
	public StpSession queryStpSession(String accountId)
			throws SupSocketException;

	public StpSession queryStpSessionByTicket(String sessionTicket)
			throws SupSocketException;

	/**
	 * one account, one session(can't login by multiply devices at the same
	 * time)
	 */
	public GateSession queryGateSession(String deviceId)
			throws SupSocketException;

	public boolean removeStpSession(String accountId)
			throws SupSocketException;

	public boolean removeStpSessionByTicket(String sessionTicket)
			throws SupSocketException;

	public boolean inactiveStpSessionByTicket(String sessionTicket)
			throws SupSocketException;

	public boolean inactiveStpSession(String accountId)
			throws SupSocketException;

	public StpSession activeStpSession(String sessionTicket, long stpSession)
			throws SupSocketException;

	/**
	 * @return sessionTicket
	 */
	public String applySessionTicket(StpSession stpSession)
			throws SupSocketException;

}
