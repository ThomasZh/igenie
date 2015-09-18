package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class VerifyLoginResp
		extends SupRespCommand
{
	public VerifyLoginResp()
	{
		this.setTag(SupCommandTag.VERIFY_LOGIN_RESP);
	}

	public VerifyLoginResp(long sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	public VerifyLoginResp(long sequence, short respState, String accountId)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setAccountId(accountId);
	}

	@Override
	public VerifyLoginResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tAccountId = tlv.getChild(i++);
		this.setAccountId(new String(tAccountId.getValue(), "UTF-8"));

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tAccountId = new TlvObject(i++, accountId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tAccountId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String accountId;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(VerifyLoginResp.class);

}
