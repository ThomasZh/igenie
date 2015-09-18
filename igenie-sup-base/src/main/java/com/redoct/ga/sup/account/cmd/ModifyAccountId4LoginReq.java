package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ModifyAccountId4LoginReq
		extends SupReqCommand
{
	public ModifyAccountId4LoginReq()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_ACCOUNT_LOGIN_REQ);
	}

	public ModifyAccountId4LoginReq(String accountId, short loginType, String loginName)
	{
		this();

		this.setAccountId(accountId);
		this.setLoginType(loginType);
		this.setLoginName(loginName);
	}

	public ModifyAccountId4LoginReq(long sequence, String accountId, short loginType, String loginName)
	{
		this(accountId, loginType, loginName);

		this.setSequence(sequence);
	}

	@Override
	public ModifyAccountId4LoginReq decode(TlvObject tlv)
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

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		TlvObject tLoginType = tlv.getChild(i++);
		loginType = TlvByteUtil.byte2Short(tLoginType.getValue());
		logger.debug("loginType: " + loginType);

		TlvObject tLoginName = tlv.getChild(i++);
		loginName = new String(tLoginName.getValue(), "UTF-8");
		logger.debug("loginName: " + loginName);

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
		TlvObject tLoginName = new TlvObject(i++, loginName);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tAccountId);
		tlv.push(tLoginType);
		tlv.push(tLoginName);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String accountId;
	private short loginType;
	private String loginName;

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

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyAccountId4LoginReq.class);

}
