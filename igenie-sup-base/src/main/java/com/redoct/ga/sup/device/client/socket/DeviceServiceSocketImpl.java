package com.redoct.ga.sup.device.client.socket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionResp;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionResp;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoReq;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoResp;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class DeviceServiceSocketImpl
		implements SupDeviceService
{
	@Override
	public DeviceBasicInfo query(String deviceId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_DEVICE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryDeviceBasicInfoReq reqCmd = new QueryDeviceBasicInfoReq(deviceId);

				QueryDeviceBasicInfoResp respCmd = (QueryDeviceBasicInfoResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.debug("response state: " + respCmd.getRespState());
					DeviceBasicInfo device = respCmd.getDevice();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return device;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public void modifyClientVersion(String deviceId, String clientVersion, String appId, String vendorId, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_DEVICE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ModifyDeviceClientVersionReq reqCmd = new ModifyDeviceClientVersionReq(deviceId, clientVersion, appId,
						vendorId);

				ModifyDeviceClientVersionResp respCmd;

				respCmd = (ModifyDeviceClientVersionResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public void modifyOsVersion(String deviceId, String osVersion, String notifyToken, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_DEVICE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ModifyDeviceOsVersionReq reqCmd = new ModifyDeviceOsVersionReq(deviceId, osVersion, notifyToken);

				ModifyDeviceOsVersionResp respCmd = (ModifyDeviceOsVersionResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	// /////////////////////////////////////////////////////

	private SupSocketClient socketClient;

	public SupSocketClient getSocketClient()
	{
		return socketClient;
	}

	public void setSocketClient(SupSocketClient socketClient)
	{
		this.socketClient = socketClient;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeviceServiceSocketImpl.class);

}
