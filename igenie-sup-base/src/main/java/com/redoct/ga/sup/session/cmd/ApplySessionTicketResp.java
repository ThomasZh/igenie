package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class ApplySessionTicketResp
		extends SupRespCommand
{
	public ApplySessionTicketResp()
	{
		this.setTag(SupCommandTag.APPLY_SESSION_TICKET_RESP);
	}

	public ApplySessionTicketResp(long sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	public ApplySessionTicketResp(long sequence, short respState, String sessionTicket)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setSessionTicket(sessionTicket);
	}

	@Override
	public ApplySessionTicketResp decode(TlvObject tlv)
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

		TlvObject tSessionTicket = tlv.getChild(i++);
		sessionTicket = new String(tSessionTicket.getValue(), "UTF-8");

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tSessionTicket = new TlvObject(i++, sessionTicket);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tSessionTicket);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String sessionTicket;

	public String getSessionTicket()
	{
		return sessionTicket;
	}

	public void setSessionTicket(String sessionTicket)
	{
		this.sessionTicket = sessionTicket;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplySessionTicketResp.class);

}
