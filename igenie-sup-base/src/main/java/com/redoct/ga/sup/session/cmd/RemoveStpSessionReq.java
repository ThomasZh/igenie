package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class RemoveStpSessionReq
		extends SupReqCommand
{
	public RemoveStpSessionReq()
	{
		super();

		this.setTag(SupCommandTag.REMOVE_STP_SESSION_REQ);
	}

	public RemoveStpSessionReq(String accountId)
	{
		this();

		this.setAccountId(accountId);
	}

	public RemoveStpSessionReq(long sequence, String accountId)
	{
		this(accountId);

		this.setSequence(sequence);
	}

	@Override
	public RemoveStpSessionReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tAccountId = new TlvObject(i++, accountId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
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

	private final static Logger logger = LoggerFactory.getLogger(RemoveStpSessionReq.class);

}
