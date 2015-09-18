package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMessageBadgeNumberReq
		extends ReqCommand
{
	public QueryMessageBadgeNumberReq()
	{
		super();

		this.setTag(Command.QUERY_MESSAGE_BADGE_NUMBER_REQ);
	}

	public QueryMessageBadgeNumberReq(int sequence, int lastTryTime)
	{
		this();

		this.setSequence(sequence);
		this.setLastTryTime(lastTryTime);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tLastTryTime = new TlvObject(i++, 4, TlvByteUtil.int2Byte(lastTryTime));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tLastTryTime);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryMessageBadgeNumberReq decode(TlvObject tlv)
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

		TlvObject tTimestamp = tlv.getChild(i++);
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

	private final static Logger logger = LoggerFactory.getLogger(QueryMessageBadgeNumberReq.class);
}
