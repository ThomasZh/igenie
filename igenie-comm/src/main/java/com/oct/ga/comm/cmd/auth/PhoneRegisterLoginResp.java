package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class PhoneRegisterLoginResp
		extends RespCommand
{
	public PhoneRegisterLoginResp()
	{
		this.setTag(Command.PHONE_REGISTER_LOGIN_RESP);
	}

	public PhoneRegisterLoginResp(short respState, String accountId, String sessionToken)
	{
		this();

		this.setAccountId(accountId);
		this.setRespState(respState);
		this.setSessionToken(sessionToken);
	}

	@Override
	public PhoneRegisterLoginResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tAccountId = tlv.getChild(i++);
		this.setAccountId(new String(tAccountId.getValue(), "UTF-8"));

		TlvObject tSessionToken = tlv.getChild(i++);
		this.setSessionToken(new String((tSessionToken.getValue()), "UTF-8"));

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tAccountId = new TlvObject(i++, accountId);
		TlvObject tSessionToken = new TlvObject(i++, sessionToken);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tAccountId);
		tlv.push(tSessionToken);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String accountId;
	private String sessionToken;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getSessionToken()
	{
		return sessionToken;
	}

	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(PhoneRegisterLoginResp.class);

}
