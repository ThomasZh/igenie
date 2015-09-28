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
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoReq;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoResp;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class QueryDeviceBasicInfoAdapter
		extends SupReqCommand
{
	public QueryDeviceBasicInfoAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_DEVICE_INFO_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryDeviceBasicInfoReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		QueryDeviceBasicInfoResp respCmd = null;

		try {
			SupDeviceService deviceService = (SupDeviceService) context.getBean("supDeviceService");
			DeviceCacheManager deviceCacheManager = GenericSingleton.getInstance(DeviceCacheManager.class);

			DeviceBasicInfo device = deviceCacheManager.getDevice(deviceId);
			if (device == null) {
				device = deviceService.query(deviceId);
				deviceCacheManager.putDevice(deviceId, device);
			}

			respCmd = new QueryDeviceBasicInfoResp(this.getSequence(), ErrorCode.SUCCESS, device);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryDeviceBasicInfoResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryDeviceBasicInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryDeviceBasicInfoAdapter.class);

}
