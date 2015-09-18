package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryStpStatesReq
		extends StpCommand
{
	public QueryStpStatesReq()
	{
		super();

		this.setTag(Command.GK_QUERY_STP_STATES_REQ);
	}

	public QueryStpStatesReq(int sequence)
	{
		this();

		this.sequence = sequence;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tlv = new TlvObject(Command.GK_QUERY_STP_STATES_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));

		tlv.push(tSequence);

		return tlv;
	}

	@Override
	public QueryStpStatesReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.GK_MODIFY_STP_STATE_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		return this;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryStpStatesReq.class);

}
