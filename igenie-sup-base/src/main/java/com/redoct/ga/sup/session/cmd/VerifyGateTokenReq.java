package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class VerifyGateTokenReq
		extends SupReqCommand
{
	public VerifyGateTokenReq()
	{
		super();

		this.setTag(SupCommandTag.VERIFY_GATE_TOKEN_REQ);
	}

	public VerifyGateTokenReq(String gateToken, String deviceId)
	{
		this();

		this.setGateToken(gateToken);
		this.setDeviceId(deviceId);
	}

	public VerifyGateTokenReq(long sequence, String gateToken, String deviceId)
	{
		this(gateToken, deviceId);

		this.setSequence(sequence);
	}

	@Override
	public VerifyGateTokenReq decode(TlvObject tlv)
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

		TlvObject tGateToken = tlv.getChild(i++);
		gateToken = new String(tGateToken.getValue(), "UTF-8");
		logger.debug("gateToken: " + gateToken);

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
		TlvObject tGateToken = new TlvObject(i++, gateToken);
		TlvObject tDeviceId = new TlvObject(i++, deviceId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tGateToken);
		tlv.push(tDeviceId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String gateToken;
	private String deviceId;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(VerifyGateTokenReq.class);

}
