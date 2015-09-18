package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class InactiveStpSessionByTicketReq
		extends SupReqCommand
{
	public InactiveStpSessionByTicketReq()
	{
		super();

		this.setTag(SupCommandTag.INACTIVE_STP_SESSION_BY_TICKET_REQ);
	}

	public InactiveStpSessionByTicketReq(String accountId)
	{
		this();

		this.setSessionTicket(accountId);
	}

	public InactiveStpSessionByTicketReq(long sequence, String accountId)
	{
		this(accountId);

		this.setSequence(sequence);
	}

	@Override
	public InactiveStpSessionByTicketReq decode(TlvObject tlv)
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
		sessionTicket = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("sessionTicket: " + sessionTicket);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tAccountId = new TlvObject(i++, sessionTicket);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tAccountId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String sessionTicket;

	public String getSessionTicket()
	{
		return sessionTicket;
	}

	public void setSessionTicket(String accountId)
	{
		this.sessionTicket = accountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(InactiveStpSessionByTicketReq.class);

}
