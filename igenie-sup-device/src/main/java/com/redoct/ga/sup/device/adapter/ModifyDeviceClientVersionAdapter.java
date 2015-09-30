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
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionResp;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class ModifyDeviceClientVersionAdapter
		extends SupReqCommand
{
	public ModifyDeviceClientVersionAdapter()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyDeviceClientVersionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String clientVersion = reqCmd.getClientVersion();
		String appId = reqCmd.getAppId();
		String vendorId = reqCmd.getVendorId();
		ModifyDeviceClientVersionResp respCmd = null;

		try {
			SupDeviceService deviceService = (SupDeviceService) context.getBean("supDeviceService");
			DeviceCacheManager deviceCacheManager = GenericSingleton.getInstance(DeviceCacheManager.class);

			DeviceBasicInfo device = deviceCacheManager.getDevice(deviceId);
			if (device == null) { // no deviceInfo in memcached
				logger.warn("has no device=[" + deviceId + "] in memcached");

				device = deviceService.query(deviceId);
				if (device.getDeviceId() == null) { // no deviceInfo in database
					logger.warn("has no device=[" + deviceId + "] in database");

					// add it into database
					deviceService.modifyClientVersion(deviceId, clientVersion, appId, vendorId,
							this.getCurrentTimestamp());

					// add modify info(clientVersion) log
					logger.info("add device=[" + deviceId + "] clientVersion=[" + device.getClientVersion()
							+ "], appId =[appId]. vendorId=[" + device.getVendorId() + "]");

					// add it into memcached
					device.setDeviceId(deviceId);
					device.setClientVersion(clientVersion);
					device.setAppId(appId);
					device.setVendorId(vendorId);
					deviceCacheManager.putDevice(deviceId, device);
				} else { // has deviceInfo in db, but not in memcached
					if (clientVersion.equals(device.getClientVersion()) && appId.equals(device.getAppId())
							&& vendorId.equals(device.getVendorId())) {
						// add it into memcached
						deviceCacheManager.putDevice(deviceId, device);
					} else {
						// update it in database
						deviceService.modifyClientVersion(deviceId, clientVersion, appId, vendorId,
								this.getCurrentTimestamp());

						// add modify info(clientVersion) log
						logger.info("modify device=[" + deviceId + "] clientVersion from=[" + device.getClientVersion()
								+ "] to=[" + clientVersion + "], appId from=[" + device.getAppId()
								+ "] to=[appId]. vendorId from=[" + device.getVendorId() + "] to=["
								+ device.getVendorId() + "]");

						// update it in memcached
						device.setClientVersion(clientVersion);
						device.setAppId(appId);
						device.setVendorId(vendorId);
						deviceCacheManager.putDevice(deviceId, device);
					}
				}
			} else { // has deviceInfo in memcached
				logger.debug("has a device=[" + deviceId + "] in memcached");

				if (clientVersion.equals(device.getClientVersion()) && appId.equals(device.getAppId())
						&& vendorId.equals(device.getVendorId())) {
					; // do nothing
				} else {
					// update it in database
					deviceService.modifyClientVersion(deviceId, clientVersion, appId, vendorId,
							this.getCurrentTimestamp());

					// add modify info(clientVersion) log
					logger.info("modify device=[" + deviceId + "] clientVersion from=[" + device.getClientVersion()
							+ "] to=[" + clientVersion + "], appId from=[" + device.getAppId()
							+ "] to=[appId]. vendorId from=[" + device.getVendorId() + "] to=[" + device.getVendorId()
							+ "]");

					// update it in memcached
					device.setClientVersion(clientVersion);
					device.setAppId(appId);
					device.setVendorId(vendorId);
					deviceCacheManager.putDevice(deviceId, device);
				}
			}

			respCmd = new ModifyDeviceClientVersionResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyDeviceClientVersionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyDeviceClientVersionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyDeviceClientVersionAdapter.class);

}
