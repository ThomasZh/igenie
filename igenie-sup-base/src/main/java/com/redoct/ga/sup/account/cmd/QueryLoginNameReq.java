package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class QueryLoginNameReq
		extends SupReqCommand
{
	public QueryLoginNameReq()
	{
		super();

		this.setTag(SupCommandTag.QUERY_LOGIN_NAME_REQ);
	}

	public QueryLoginNameReq(String accountId, short loginType)
	{
		this();

		this.setAccountId(accountId);
		this.setLoginType(loginType);
	}

	public QueryLoginNameReq(long sequence, String accountId, short loginType)
	{
		this(accountId, loginType);

		this.setSequence(sequence);
	}

	@Override
	public QueryLoginNameReq decode(TlvObject tlv)
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

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		TlvObject tLoginType = tlv.getChild(i++);
		loginType = TlvByteUtil.byte2Short(tLoginType.getValue());
		logger.debug("loginType: " + loginType);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tAccountId = new TlvObject(i++, accountId);
		TlvObject tLoginType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(loginType));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tAccountId);
		tlv.push(tLoginType);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String accountId;
	private short loginType;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public short getLoginType()
	{
		return loginType;
	}

	public void setLoginType(short loginType)
	{
		this.loginType = loginType;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryLoginNameReq.class);

}
