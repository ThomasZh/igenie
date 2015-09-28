package com.oct.ga.session;

import java.util.Set;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public interface GaSessionService
{
	// ##############################################################
	// Map1:GaAccountDeviceState:
	// (key)accountId+deviceId:(value)online+lastTryTime
	// //////////////////////////////////////////////////////////////

	/**
	 * online: client调用login或STP_ARQ给server(account+device)
	 * 
	 * @param accountId
	 * @param deivceId
	 */
	public void active(String accountId, String deviceId);

	/**
	 * offline: client调用disconnect给server(account+device)
	 * 
	 * @param accountId
	 * @param deivceId
	 */
	public void inactive(String accountId, String deviceId);

	/**
	 * account+device
	 * 
	 * @param accountId
	 * @param deivceId
	 * @return
	 */
	public boolean isOnline(String accountId, String deviceId);

	/**
	 * account+device
	 * 
	 * @param accountId
	 * @param deivceId
	 * @return
	 */
	public int getLastTryTime(String accountId, String deviceId);

	// //////////////////////////////////////////////////////////////
	// End Map1:GaAccountDeviceState
	// ##############################################################

	// ##############################################################
	// Map2:GaSessionInfo:
	// (key)deviceId:(value)sessionInfo
	// //////////////////////////////////////////////////////////////

	// @Override
	public String getAccountId(String deviceId);

	// @Override
	public String getAccountName(String deviceId);

	public long getIoSessionId(String deviceId);

	public GaSessionInfo getSession(String deviceId);

	public void putSession(String deviceId, GaSessionInfo session);

	/**
	 * 
	 * @param deivceId
	 * @return
	 */
	public int getLastTryTime(String deviceId);

	// assert last time is another account use this device,
	// inactive the other account use this device is offline state.

	// //////////////////////////////////////////////////////////////
	// End Map2:GaSessionInfo
	// ##############################################################

	// ##############################################################
	// Map3:DeviceList:
	// (key)accountId:(value)deviceSet
	// //////////////////////////////////////////////////////////////

	// @Override
	public void putSession(String accountId, String deviceId, GaSessionInfo session);

	/**
	 * client调用logout给server(account+device)
	 * 
	 * @param accountId
	 * @param deviceId
	 */
	public void removeDevice(String accountId, String deviceId);

	/**
	 * 取得设备列表
	 * 
	 * @param accountId
	 * @return
	 */
	public Set<String> getDeviceList(String accountId);

	// //////////////////////////////////////////////////////////////
	// End Map3:DeviceList
	// ##############################################################

	// ##############################################################
	// Map4:StpServerInfo:
	// (key)stpId:(value)stp
	// //////////////////////////////////////////////////////////////

	public void putStp(String stpId, StpServerInfoJsonBean stp);

	public StpServerInfoJsonBean getStp(String stpId);

	// //////////////////////////////////////////////////////////////
	// End Map4:StpServerInfo
	// ##############################################################

	// //////////////////////////////////////////////////////////////

	public void init();
}
