package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class STP_ARQ
		extends ReqCommand
{
	public STP_ARQ()
	{
		super();

		this.setTag(Command.STP_ARQ);
	}

	@Override
	public STP_ARQ decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tDeviceId = tlv.getChild(i++);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		TlvObject tSessionToken = tlv.getChild(i++);
		sessionToken = new String(tSessionToken.getValue(), "UTF-8");
		logger.debug("sessionToken: " + sessionToken);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tDeviceId = new TlvObject(i++, deviceId);
		TlvObject tSessionToken = new TlvObject(i++, sessionToken);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tDeviceId);
		tlv.push(tSessionToken);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	String deviceId;
	String sessionToken;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getSessionToken()
	{
		return sessionToken;
	}

	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(STP_ARQ.class);
}
