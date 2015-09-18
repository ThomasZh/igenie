package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ApplyPhoneRegisterVerificationCodeReq
		extends ReqCommand
{
	public ApplyPhoneRegisterVerificationCodeReq()
	{
		super();

		this.setTag(Command.APPLY_PHONE_REGISTER_VERIFICATION_CODE_REQ);
	}

	public ApplyPhoneRegisterVerificationCodeReq(String deviceId, String phone, String lang)
	{
		this();

		this.setDeviceId(deviceId);
		this.setPhone(phone);
		this.setLang(lang);
	}

	public ApplyPhoneRegisterVerificationCodeReq(int sequence, String deviceId, String phone, String lang)
	{
		this(deviceId, phone, lang);

		this.setSequence(sequence);
	}

	@Override
	public ApplyPhoneRegisterVerificationCodeReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tPhone = tlv.getChild(i++);
		phone = new String(tPhone.getValue(), "UTF-8");
		logger.debug("phone: " + phone);

		TlvObject tLang = tlv.getChild(i++);
		lang = new String(tLang.getValue(), "UTF-8");
		logger.debug("lang: " + lang);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tDeviceId = new TlvObject(i++, deviceId);
		TlvObject tPhone = new TlvObject(i++, phone);
		TlvObject tLang = new TlvObject(i++, lang);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tDeviceId);
		tlv.push(tPhone);
		tlv.push(tLang);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String deviceId;
	private String phone;
	private String lang;

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

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyPhoneRegisterVerificationCodeReq.class);

}
