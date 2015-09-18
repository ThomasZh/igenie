package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class PhoneRegisterLoginReq
		extends ReqCommand
{
	public PhoneRegisterLoginReq()
	{
		super();

		this.setTag(Command.PHONE_REGISTER_LOGIN_REQ);
	}

	@Override
	public PhoneRegisterLoginReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 9;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tOsVersion = tlv.getChild(i++);
		osVersion = new String(tOsVersion.getValue(), "UTF-8");
		logger.debug("osVersion: " + osVersion);

		TlvObject tGateToken = tlv.getChild(i++);
		gateToken = new String(tGateToken.getValue(), "UTF-8");
		logger.debug("gateToken: " + gateToken);

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tApnsToken = tlv.getChild(i++);
		apnsToken = new String(tApnsToken.getValue(), "UTF-8");
		logger.debug("apnsToken: " + apnsToken);

		TlvObject tPhone = tlv.getChild(i++);
		phone = new String(tPhone.getValue(), "UTF-8");
		logger.debug("phone: " + phone);

		TlvObject tPassword = tlv.getChild(i++);
		md5pwd = new String(tPassword.getValue(), "UTF-8");

		TlvObject tVerificationCode = tlv.getChild(i++);
		verificationCode = new String(tVerificationCode.getValue(), "UTF-8");
		logger.debug("verificationCode: " + verificationCode);

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

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tOsVersion = new TlvObject(i++, osVersion);
		TlvObject tGateToken = new TlvObject(i++, gateToken);
		TlvObject tDeviceId = new TlvObject(i++, deviceId);
		TlvObject tApnsToken = new TlvObject(i++, apnsToken);
		TlvObject tPhone = new TlvObject(i++, phone);
		TlvObject tPassword = new TlvObject(i++, md5pwd);
		TlvObject tVerificationCode = new TlvObject(i++, verificationCode);
		TlvObject tLang = new TlvObject(i++, lang);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tOsVersion);
		tlv.push(tGateToken);
		tlv.push(tDeviceId);
		tlv.push(tApnsToken);
		tlv.push(tPhone);
		tlv.push(tPassword);
		tlv.push(tVerificationCode);
		tlv.push(tLang);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String apnsToken;
	private String phone;
	private String md5pwd;
	private String verificationCode;
	private String lang;

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getMd5pwd()
	{
		return md5pwd;
	}

	public void setMd5pwd(String password)
	{
		this.md5pwd = password;
	}

	public String getApnsToken()
	{
		return apnsToken;
	}

	public void setApnsToken(String apnsToken)
	{
		this.apnsToken = apnsToken;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getVerificationCode()
	{
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode)
	{
		this.verificationCode = verificationCode;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	private final static Logger logger = LoggerFactory.getLogger(PhoneRegisterLoginReq.class);

}
