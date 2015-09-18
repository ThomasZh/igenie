package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SsoLoginReq
		extends ReqCommand
{
	public SsoLoginReq()
	{
		super();

		this.setTag(Command.SSO_LOGIN_REQ);
	}

	@Override
	public SsoLoginReq decode(TlvObject tlv)
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

		TlvObject tNickname = tlv.getChild(i++);
		nickname = new String(tNickname.getValue(), "UTF-8");
		logger.debug("nickname: " + nickname);

		TlvObject tDesc = tlv.getChild(i++);
		desc = new String(tDesc.getValue(), "UTF-8");
		logger.debug("desc: " + desc);

		TlvObject tLoginType = tlv.getChild(i++);
		loginType = TlvByteUtil.byte2Short(tLoginType.getValue());
		logger.debug("loginType: " + loginType);

		TlvObject tLoginName = tlv.getChild(i++);
		loginName = new String(tLoginName.getValue(), "UTF-8");
		logger.debug("loginName: " + loginName);

		TlvObject tImageUrl = tlv.getChild(i++);
		imageUrl = new String(tImageUrl.getValue(), "UTF-8");
		logger.debug("imageUrl: " + imageUrl);

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
		TlvObject tNickname = new TlvObject(i++, nickname);
		TlvObject tDesc = new TlvObject(i++, desc);
		TlvObject tLoginType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(loginType));
		TlvObject tLoginName = new TlvObject(i++, loginName);
		TlvObject tImageUrl = new TlvObject(i++, imageUrl);
		TlvObject tApnsToken = new TlvObject(i++, apnsToken);
		TlvObject tLang = new TlvObject(i++, lang);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tOsVersion);
		tlv.push(tGateToken);
		tlv.push(tDeviceId);
		tlv.push(tNickname);
		tlv.push(tDesc);
		tlv.push(tLoginType);
		tlv.push(tLoginName);
		tlv.push(tImageUrl);
		tlv.push(tApnsToken);
		tlv.push(tLang);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String nickname;
	private String desc;
	private short loginType;
	private String loginName;
	private String imageUrl;
	private String apnsToken;
	private String lang;

	public short getLoginType()
	{
		return loginType;
	}

	public void setLoginType(short loginType)
	{
		this.loginType = loginType;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

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

	public String getApnsToken()
	{
		return apnsToken;
	}

	public void setApnsToken(String apnsToken)
	{
		this.apnsToken = apnsToken;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	private final static Logger logger = LoggerFactory.getLogger(SsoLoginReq.class);

}
