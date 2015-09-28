package com.redoct.ga.sup.device.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.device.DeviceCacheManager;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionResp;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class ModifyDeviceOsVersionAdapter
		extends SupReqCommand
{
	public ModifyDeviceOsVersionAdapter()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_DEVICE_OS_VERSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyDeviceOsVersionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String osVersion = reqCmd.getOsVersion();
		String notifyToken = reqCmd.getNotifyToken();
		ModifyDeviceOsVersionResp respCmd = null;

		try {
			SupDeviceService deviceService = (SupDeviceService) context.getBean("supDeviceService");
			DeviceCacheManager deviceCacheManager = GenericSingleton.getInstance(DeviceCacheManager.class);

			DeviceBasicInfo device = deviceCacheManager.getDevice(deviceId);
			if (device == null) { // no deviceInfo in memcached
				logger.warn("has no device=[" + deviceId + "] in memcached");

				device = deviceService.query(deviceId);
				if (device.getDeviceId() == null) { // no deviceInfo in database
					logger.warn("has no device=[" + deviceId + "] in database");

					// update it in database
					deviceService.modifyOsVersion(deviceId, osVersion, notifyToken, this.getCurrentTimestamp());

					// add modify info(osVersion,notifyToken) log
					logger.info("add device=[" + deviceId + "] osVersion=[" + osVersion + "], and notifyToken=["
							+ notifyToken + "]");

					// update it in memcached
					device.setDeviceId(deviceId);
					device.setOsVersion(osVersion);
					device.setNotifyToken(notifyToken);
					deviceCacheManager.putDevice(deviceId, device);
				} else {
					logger.debug("has a device=[" + deviceId + "] in database");
					
					if (osVersion.equals(device.getOsVersion()) && notifyToken.equals(device.getNotifyToken())) {
						// add it into memcached
						deviceCacheManager.putDevice(deviceId, device);
					} else {
						// update it in database
						deviceService.modifyOsVersion(deviceId, osVersion, notifyToken, this.getCurrentTimestamp());

						// add modify info(osVersion,notifyToken) log
						logger.warn("modify device=[" + deviceId + "] osVersion from=[" + device.getOsVersion()
								+ "] to=[" + osVersion + "], and notifyToken from=[" + device.getNotifyToken()
								+ "] to=[" + notifyToken + "]");

						// update it in memcached
						device.setOsVersion(osVersion);
						device.setNotifyToken(notifyToken);
						deviceCacheManager.putDevice(deviceId, device);
					}
				}
			} else { // has deviceInfo in memcached
				logger.debug("has a device=[" + deviceId + "] in memcached");

				if (osVersion.equals(device.getOsVersion()) && notifyToken.equals(device.getNotifyToken())) {
					; // do nothing
				} else {
					// update it in database
					deviceService.modifyOsVersion(deviceId, osVersion, notifyToken, this.getCurrentTimestamp());

					// add modify info(osVersion,notifyToken) log
					logger.warn("modify device=[" + deviceId + "] osVersion from=[" + device.getOsVersion() + "] to=["
							+ osVersion + "], and notifyToken from=[" + device.getNotifyToken() + "] to=["
							+ notifyToken + "]");

					// update it in memcached
					device.setOsVersion(osVersion);
					device.setNotifyToken(notifyToken);
					deviceCacheManager.putDevice(deviceId, device);
				}
			}

			respCmd = new ModifyDeviceOsVersionResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyDeviceOsVersionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyDeviceOsVersionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyDeviceOsVersionAdapter.class);

}
