package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ApplyVerificationCodeReq
		extends SupReqCommand
{
	public ApplyVerificationCodeReq()
	{
		super();

		this.setTag(SupCommandTag.APPLY_VERIFICATION_CODE_REQ);
	}

	public ApplyVerificationCodeReq(short type, String deviceId, String phone)
	{
		this();

		this.setVerificationType(type);
		this.setDeviceId(deviceId);
		this.setPhone(phone);
	}

	public ApplyVerificationCodeReq(long sequence, short type, String deviceId, String phone)
	{
		this(type, deviceId, phone);

		this.setSequence(sequence);
	}

	@Override
	public ApplyVerificationCodeReq decode(TlvObject tlv)
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

		TlvObject tType = tlv.getChild(i++);
		verificationType = TlvByteUtil.byte2Short(tType.getValue());
		logger.debug("verificationType: " + verificationType);

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tPhone = tlv.getChild(i++);
		phone = new String(tPhone.getValue(), "UTF-8");
		logger.debug("phone: " + phone);

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
		TlvObject tPhone = new TlvObject(i++, phone);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tType);
		tlv.push(tDeviceId);
		tlv.push(tPhone);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short verificationType;
	private String deviceId;
	private String phone;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public short getVerificationType()
	{
		return verificationType;
	}

	public void setVerificationType(short verificationType)
	{
		this.verificationType = verificationType;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyVerificationCodeReq.class);

}
