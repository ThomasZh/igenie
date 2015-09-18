package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

/**
 * check user email and password, response to client
 * 
 * @author liwenzhi
 * 
 */
public class LoginReq
		extends ReqCommand
{
	public LoginReq()
	{
		super();

		this.setTag(Command.LOGIN_REQ);
	}

	public LoginReq(String osVersion, String gateToken, String deviceId, String email, String password, String tokenId)
	{
		this();

		this.setOsVersion(osVersion);
		this.setGateToken(gateToken);
		this.setMyDeviceId(deviceId);
		this.setEmail(email);
		this.setPassword(password);
		this.setApnsToken(tokenId);
	}

	@Override
	public LoginReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 7;
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

		TlvObject tEmail = tlv.getChild(i++);
		email = new String(tEmail.getValue(), "UTF-8");
		logger.debug("email: " + email);

		TlvObject tPassword = tlv.getChild(i++);
		password = new String(tPassword.getValue(), "UTF-8");

		TlvObject tApnsToken = tlv.getChild(i++);
		apnsToken = new String(tApnsToken.getValue(), "UTF-8");
		logger.debug("apnsToken: " + apnsToken);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tOsVersion = new TlvObject(i++, this.getOsVersion());
		TlvObject tClientVersion = new TlvObject(i++, this.getGateToken());
		TlvObject tDeviceId = new TlvObject(i++, this.getMyDeviceId());
		TlvObject tEmail = new TlvObject(i++, this.getEmail());
		TlvObject tPassword = new TlvObject(i++, this.getPassword());
		TlvObject tTokenId = new TlvObject(i++, this.getApnsToken());

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tOsVersion);
		tlv.push(tClientVersion);
		tlv.push(tDeviceId);
		tlv.push(tEmail);
		tlv.push(tPassword);
		tlv.push(tTokenId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String osVersion;
	private String gateToken;
	private String deviceId;
	private String apnsToken;
	private String password;

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getMyDeviceId()
	{
		return deviceId;
	}

	public void setMyDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	private String email;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	public String getApnsToken()
	{
		return apnsToken;
	}

	public void setApnsToken(String apnsToken)
	{
		this.apnsToken = apnsToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(LoginReq.class);

}
