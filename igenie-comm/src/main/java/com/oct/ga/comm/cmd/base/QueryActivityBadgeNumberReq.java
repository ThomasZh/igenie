package com.oct.ga.comm.cmd.base;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryActivityBadgeNumberReq
		extends ReqCommand
{
	public QueryActivityBadgeNumberReq()
	{
		super();

		this.setTag(Command.QUERY_ACTIVITY_BADGE_NUMBER_REQ);
	}

	public QueryActivityBadgeNumberReq(int lastTryTime)
	{
		this();

		this.setLastTryTime(lastTryTime);
	}

	@Override
	public QueryActivityBadgeNumberReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.debug("from tlv:(tag=" + Command.QUERY_ACTIVITY_BADGE_NUMBER_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTimestamp = tlv.getChild(1);
		lastTryTime = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("lastTryTime: " + lastTryTime);

		return this;
	}

	private int lastTryTime;

	public int getLastTryTime()
	{
		return lastTryTime;
	}

	public void setLastTryTime(int timestamp)
	{
		this.lastTryTime = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryActivityBadgeNumberReq.class);
}
