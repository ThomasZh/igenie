package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncFollowingReq
		extends ReqCommand
{
	public SyncFollowingReq()
	{
		super();

		this.setTag(Command.SYNC_FOLLOWING_REQ);
	}

	public SyncFollowingReq(int sequence, int lastTryTime)
	{
		this();

		this.setSequence(sequence);
		this.setLastTryTime(lastTryTime);
	}

	@Override
	public SyncFollowingReq decode(TlvObject tlv)
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

		TlvObject tLastTryTime = tlv.getChild(i++);
		lastTryTime = TlvByteUtil.byte2Int(tLastTryTime.getValue());
		logger.debug("lastTryTime: " + lastTryTime);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tLastTryTime = new TlvObject(i++, 4, TlvByteUtil.int2Byte(lastTryTime));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tLastTryTime);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private int lastTryTime;

	public int getLastTryTime()
	{
		return lastTryTime;
	}

	public void setLastTryTime(int lastTryTime)
	{
		this.lastTryTime = lastTryTime;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncFollowingReq.class);

}
