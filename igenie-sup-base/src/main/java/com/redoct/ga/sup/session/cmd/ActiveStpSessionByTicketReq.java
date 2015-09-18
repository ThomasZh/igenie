package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ActiveStpSessionByTicketReq
		extends SupReqCommand
{
	public ActiveStpSessionByTicketReq()
	{
		super();

		this.setTag(SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_REQ);
	}

	public ActiveStpSessionByTicketReq(String sessionTicket, long ioSessionId)
	{
		this();

		this.setSessionTicket(sessionTicket);
		this.setIoSessionId(ioSessionId);
	}

	public ActiveStpSessionByTicketReq(long sequence, String sessionTicket, long ioSessionId)
	{
		this(sessionTicket, ioSessionId);

		this.setSequence(sequence);
	}

	@Override
	public ActiveStpSessionByTicketReq decode(TlvObject tlv)
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

		TlvObject tSessionTicket = tlv.getChild(i++);
		sessionTicket = new String(tSessionTicket.getValue(), "UTF-8");
		logger.debug("sessionTicket: " + sessionTicket);

		TlvObject tIoSessionId = tlv.getChild(i++);
		ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tSessionTicket = new TlvObject(i++, sessionTicket);
		TlvObject tIoSessionId = new TlvObject(i++, 8, TlvByteUtil.long2Byte(ioSessionId));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tSessionTicket);
		tlv.push(tIoSessionId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String sessionTicket;
	private long ioSessionId;

	public String getSessionTicket()
	{
		return sessionTicket;
	}

	public void setSessionTicket(String sessionTicket)
	{
		this.sessionTicket = sessionTicket;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActiveStpSessionByTicketReq.class);

}
