package com.redoct.ga.sup.device.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.device.domain.DeviceBasicInfo;

public class QueryDeviceBasicInfoResp
		extends SupRespCommand
{
	public QueryDeviceBasicInfoResp()
	{
		super();

		this.setTag(SupCommandTag.QUERY_DEVICE_INFO_RESP);
	}

	public QueryDeviceBasicInfoResp(long sequence, short state)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(state);
	}

	public QueryDeviceBasicInfoResp(long sequence, short state, DeviceBasicInfo device)
	{
		this(sequence, state);

		this.setDevice(device);
	}

	@Override
	public QueryDeviceBasicInfoResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			device = gson.fromJson(json, DeviceBasicInfo.class);

			logger.debug("deviceId: " + device.getDeviceId());
			logger.debug("clientVersion: " + device.getClientVersion());
			logger.debug("osVersion: " + device.getOsVersion());
			logger.debug("notifyToken: " + device.getNotifyToken());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(device, DeviceBasicInfo.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private DeviceBasicInfo device;

	public DeviceBasicInfo getDevice()
	{
		return device;
	}

	public void setDevice(DeviceBasicInfo device)
	{
		this.device = device;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryDeviceBasicInfoResp.class);

}
