package com.oct.ga.session;

import java.util.Set;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public class SessionService3MapImpl
		implements GaSessionService
{

	// ##############################################################
	// Map1:GaAccountDeviceState:
	// (key)accountId+deviceId:(value)online+lastTryTime
	// //////////////////////////////////////////////////////////////

	/**
	 * client send login or STP_ARQ command to server, account+device(online)
	 * 
	 * @param accountId
	 * @param deivceId
	 */
	@Override
	public void active(String accountId, String deviceId)
	{
		// clean old user's session
		GaSessionInfo oldSession = sessionCache.getSession(deviceId);
		if (oldSession != null) {
			String oldAccountId = oldSession.getAccountId();
			if (! accountId.equals(oldAccountId)) {
				sessionCache.removeDevice(oldAccountId, deviceId);
			}
		}

		// create new session
		GaAccountDeviceState state = sessionCache.getState(accountId, deviceId);
		if (state == null)
			state = new GaAccountDeviceState();

		state.setOnline(true);
		state.setLastTryTime(DatetimeUtil.currentTimestamp());
		sessionCache.putState(accountId, deviceId, state);
	}

	/**
	 * client send disconnect to server, account+device(offline)
	 * 
	 * @param accountId
	 * @param deivceId
	 */
	@Override
	public void inactive(String accountId, String deviceId)
	{
		GaAccountDeviceState state = sessionCache.getState(accountId, deviceId);
		if (state == null)
			state = new GaAccountDeviceState();

		state.setOnline(false);
		state.setLastTryTime(DatetimeUtil.currentTimestamp());
		sessionCache.putState(accountId, deviceId, state);
	}

	/**
	 * accountʹ this device is online
	 * 
	 * @param accountId
	 * @param deivceId
	 * @return
	 */
	@Override
	public boolean isOnline(String accountId, String deviceId)
	{
		GaAccountDeviceState state = sessionCache.getState(accountId, deviceId);
		if (state != null)
			return state.isOnline();
		else
			return false;
	}

	/**
	 * accountʹs this device last active time
	 * 
	 * @param accountId
	 * @param deivceId
	 * @return
	 */
	@Override
	public int getLastTryTime(String accountId, String deviceId)
	{
		GaAccountDeviceState state = sessionCache.getState(accountId, deviceId);
		if (state != null)
			return state.getLastTryTime();
		else
			return 0;
	}

	// //////////////////////////////////////////////////////////////
	// End Map1:GaAccountDeviceState
	// ##############################################################

	// ##############################################################
	// Map2:GaSessionInfo:
	// (key)deviceId:(value)sessionInfo
	// //////////////////////////////////////////////////////////////

	@Override
	public String getAccountId(String deviceId)
	{
		GaSessionInfo session = sessionCache.getSession(deviceId);
		if (session != null)
			return session.getAccountId();
		else
			return null;
	}

	@Override
	public String getAccountName(String deviceId)
	{
		GaSessionInfo session = sessionCache.getSession(deviceId);
		if (session != null)
			return session.getAccountName();
		else
			return null;
	}

	@Override
	public long getIoSessionId(String deviceId)
	{
		GaSessionInfo session = sessionCache.getSession(deviceId);
		if (session != null)
			return session.getIoSessionId();
		else
			return 0;
	}

	@Override
	public GaSessionInfo getSession(String deviceId)
	{
		return sessionCache.getSession(deviceId);
	}

	@Override
	public void putSession(String deviceId, GaSessionInfo session)
	{
		sessionCache.putSession(deviceId, session);
	}

	/**
	 * this device last active time
	 * 
	 * @param deivceId
	 * @return
	 */
	@Override
	public int getLastTryTime(String deviceId)
	{
		String lastLoginAccountId = getAccountId(deviceId);
		if (lastLoginAccountId == null || lastLoginAccountId.length() == 0)
			return 0;
		else
			return getLastTryTime(lastLoginAccountId, deviceId);
	}

	// assert last time is another account use this device,
	// inactive the other account use this device is offline state.

	// //////////////////////////////////////////////////////////////
	// End Map2:GaSessionInfo
	// ##############################################################

	// ##############################################################
	// Map3:DeviceList:
	// (key)accountId:(value)deviceSet
	// //////////////////////////////////////////////////////////////

	@Override
	public void putSession(String accountId, String deviceId, GaSessionInfo session)
	{
		sessionCache.putDevice(accountId, deviceId);
		sessionCache.putSession(deviceId, session);
	}

	/**
	 * client send logout command to server, remove this device from account's
	 * device list
	 * 
	 * @param accountId
	 * @param deviceId
	 */
	@Override
	public void removeDevice(String accountId, String deviceId)
	{
		sessionCache.removeDevice(accountId, deviceId);
	}

	/**
	 * get account has used device's list
	 * 
	 * @param accountId
	 * @return
	 */
	@Override
	public Set<String> getDeviceList(String accountId)
	{
		return sessionCache.getDeviceList(accountId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map3:DeviceList
	// ##############################################################

	// ##############################################################
	// Map4:StpServerInfo:
	// (key)stpId:(value)stp
	// //////////////////////////////////////////////////////////////

	@Override
	public void putStp(String stpId, StpServerInfoJsonBean stp)
	{
		sessionCache.putStp(stpId, stp);
	}

	@Override
	public StpServerInfoJsonBean getStp(String stpId)
	{
		return sessionCache.getStp(stpId);
	}

	// //////////////////////////////////////////////////////////////
	// End Map4:StpServerInfo
	// ##############################################################

	// //////////////////////////////////////////////////////////////

	@Override
	public void init()
	{
		sessionCache = GenericSingleton.getInstance(SessionCache3MapImpl.class);
		sessionCache.init();
	}

	private SessionCache3MapImpl sessionCache;
}
