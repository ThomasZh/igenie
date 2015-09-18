package com.redoct.ga.sup.device.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ModifyDeviceClientVersionReq
		extends SupReqCommand
{
	public ModifyDeviceClientVersionReq()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_REQ);
	}

	public ModifyDeviceClientVersionReq(String deviceId, String clientVersion, String appId, String vendorId)
	{
		this();

		this.setDeviceId(deviceId);
		this.setClientVersion(clientVersion);
		this.setAppId(appId);
		this.setVendorId(vendorId);
	}

	public ModifyDeviceClientVersionReq(long sequence, String deviceId, String clientVersion, String appId,
			String vendorId)
	{
		this(deviceId, clientVersion, appId, vendorId);

		this.setSequence(sequence);
	}

	@Override
	public ModifyDeviceClientVersionReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tClientVersion = tlv.getChild(i++);
		clientVersion = new String(tClientVersion.getValue(), "UTF-8");
		logger.debug("clientVersion: " + clientVersion);

		TlvObject tAppId = tlv.getChild(i++);
		appId = new String(tAppId.getValue(), "UTF-8");
		logger.debug("appId: " + appId);

		TlvObject tVendorId = tlv.getChild(i++);
		vendorId = new String(tVendorId.getValue(), "UTF-8");
		logger.debug("vendorId: " + vendorId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tDeviceId = new TlvObject(i++, deviceId);
		TlvObject tClientVersion = new TlvObject(i++, clientVersion);
		TlvObject tAppId = new TlvObject(i++, appId);
		TlvObject tVendorId = new TlvObject(i++, vendorId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tDeviceId);
		tlv.push(tClientVersion);
		tlv.push(tAppId);
		tlv.push(tVendorId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String deviceId;
	private String clientVersion;
	private String appId;
	private String vendorId;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getClientVersion()
	{
		return clientVersion;
	}

	public void setClientVersion(String clientVersion)
	{
		this.clientVersion = clientVersion;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyDeviceClientVersionReq.class);

}
