package com.redoct.ga.sup.device.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ModifyDeviceOsVersionReq
		extends SupReqCommand
{
	public ModifyDeviceOsVersionReq()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_DEVICE_OS_VERSION_REQ);
	}

	public ModifyDeviceOsVersionReq(String deviceId, String osVersion, String notifyToken)
	{
		this();

		this.setDeviceId(deviceId);
		this.setOsVersion(osVersion);
		this.setNotifyToken(notifyToken);
	}

	public ModifyDeviceOsVersionReq(long sequence, String deviceId, String osVersion, String notifyToken)
	{
		this(deviceId, osVersion, notifyToken);

		this.setSequence(sequence);
	}

	@Override
	public ModifyDeviceOsVersionReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tOsVersion = tlv.getChild(i++);
		osVersion = new String(tOsVersion.getValue(), "UTF-8");
		logger.debug("osVersion: " + osVersion);

		TlvObject tNotifyToken = tlv.getChild(i++);
		notifyToken = new String(tNotifyToken.getValue(), "UTF-8");
		logger.debug("notifyToken: " + notifyToken);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tDeviceId = new TlvObject(i++, deviceId);
		TlvObject tOsVersion = new TlvObject(i++, osVersion);
		TlvObject tNotifyToken = new TlvObject(i++, notifyToken);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tDeviceId);
		tlv.push(tOsVersion);
		tlv.push(tNotifyToken);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String deviceId;
	private String osVersion;
	private String notifyToken;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getNotifyToken()
	{
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken)
	{
		this.notifyToken = notifyToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyDeviceOsVersionReq.class);

}
