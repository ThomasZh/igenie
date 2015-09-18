package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class QueryVerificationCodeReq
		extends SupReqCommand
{
	public QueryVerificationCodeReq()
	{
		super();

		this.setTag(SupCommandTag.QUERY_VERIFICATION_CODE_REQ);
	}

	public QueryVerificationCodeReq(short type, String deviceId)
	{
		this();

		this.setVerificationType(type);
		this.setDeviceId(deviceId);
	}

	public QueryVerificationCodeReq(long sequence, short type, String deviceId)
	{
		this(type, deviceId);

		this.setSequence(sequence);
	}

	@Override
	public QueryVerificationCodeReq decode(TlvObject tlv)
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

		TlvObject tType = tlv.getChild(i++);
		verificationType = TlvByteUtil.byte2Short(tType.getValue());
		logger.debug("verificationType: " + verificationType);

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
		TlvObject tType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(verificationType));
		TlvObject tDeviceId = new TlvObject(i++, deviceId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tType);
		tlv.push(tDeviceId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short verificationType;
	private String deviceId;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public short getVerificationType()
	{
		return verificationType;
	}

	public void setVerificationType(short verificationType)
	{
		this.verificationType = verificationType;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryVerificationCodeReq.class);

}
