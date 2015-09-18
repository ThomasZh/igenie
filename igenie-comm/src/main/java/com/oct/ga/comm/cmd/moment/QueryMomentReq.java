package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMomentReq
		extends ReqCommand
{
	public QueryMomentReq()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_REQ);
	}

	public QueryMomentReq(String momentId)
	{
		this();

		this.setMomentId(momentId);
	}

	public QueryMomentReq(int sequence, String momentId)
	{
		this(momentId);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tMomentId = new TlvObject(i++, momentId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tMomentId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryMomentReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tMomentId = tlv.getChild(i++);
		momentId = new String(tMomentId.getValue(), "UTF-8");
		logger.debug("momentId: " + momentId);

		return this;
	}

	private String momentId;

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentReq.class);

}
