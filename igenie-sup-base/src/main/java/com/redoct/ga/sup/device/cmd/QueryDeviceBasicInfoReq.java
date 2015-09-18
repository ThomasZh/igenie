package com.redoct.ga.sup.device.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class QueryDeviceBasicInfoReq
		extends SupReqCommand
{
	public QueryDeviceBasicInfoReq()
	{
		super();

		this.setTag(SupCommandTag.QUERY_DEVICE_INFO_REQ);
	}

	public QueryDeviceBasicInfoReq(String deviceId)
	{
		this();

		this.setDeviceId(deviceId);
	}

	public QueryDeviceBasicInfoReq(long sequence, String deviceId)
	{
		this(deviceId);

		this.setSequence(sequence);
	}

	@Override
	public QueryDeviceBasicInfoReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tDeviceId = new TlvObject(i++, deviceId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tDeviceId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String deviceId;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryDeviceBasicInfoReq.class);

}
