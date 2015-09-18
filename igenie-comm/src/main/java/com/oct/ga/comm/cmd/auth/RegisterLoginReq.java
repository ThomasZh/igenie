package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class RegisterLoginReq
		extends ReqCommand
{
	public RegisterLoginReq()
	{
		super();

		this.setTag(Command.REGISTER_LOGIN_REQ);
	}

	@Override
	public RegisterLoginReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 11;
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

		TlvObject tFirstName = tlv.getChild(i++);
		firstName = new String(tFirstName.getValue(), "UTF-8");
		logger.debug("firstName: " + firstName);

		TlvObject tLastName = tlv.getChild(i++);
		lastName = new String(tLastName.getValue(), "UTF-8");
		logger.debug("lastname: " + lastName);

		TlvObject tEmail = tlv.getChild(i++);
		email = new String(tEmail.getValue(), "UTF-8");
		logger.debug("email: " + email);

		TlvObject tPassword = tlv.getChild(i++);
		md5pwd = new String(tPassword.getValue(), "UTF-8");

		TlvObject tFacePhoto = tlv.getChild(i++);
		facePhoto = new String(tFacePhoto.getValue(), "UTF-8");
		logger.debug("facePhoto: " + facePhoto);

		TlvObject tApnsToken = tlv.getChild(i++);
		apnsToken = new String(tApnsToken.getValue(), "UTF-8");
		logger.debug("apnsToken: " + apnsToken);

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
		TlvObject tFirstName = new TlvObject(i++, firstName);
		TlvObject tLastName = new TlvObject(i++, lastName);
		TlvObject tEmail = new TlvObject(i++, email);
		TlvObject tPassword = new TlvObject(i++, md5pwd);
		TlvObject tFacePhoto = new TlvObject(i++, facePhoto);
		TlvObject tApnsToken = new TlvObject(i++, apnsToken);
		TlvObject tLang = new TlvObject(i++, lang);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tOsVersion);
		tlv.push(tGateToken);
		tlv.push(tDeviceId);
		tlv.push(tFirstName);
		tlv.push(tLastName);
		tlv.push(tEmail);
		tlv.push(tPassword);
		tlv.push(tFacePhoto);
		tlv.push(tApnsToken);
		tlv.push(tLang);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String firstName;
	private String lastName;
	private String email;
	private String md5pwd;
	private String facePhoto;
	private String apnsToken;
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

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMd5pwd()
	{
		return md5pwd;
	}

	public void setMd5pwd(String password)
	{
		this.md5pwd = password;
	}

	public String getFacePhoto()
	{
		return facePhoto;
	}

	public void setFacePhoto(String facePhoto)
	{
		this.facePhoto = facePhoto;
	}

	public String getApnsToken()
	{
		return apnsToken;
	}

	public void setApnsToken(String apnsToken)
	{
		this.apnsToken = apnsToken;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	private final static Logger logger = LoggerFactory.getLogger(RegisterLoginReq.class);

}
