package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncTaskCompletedReq
		extends ReqCommand
{
	public SyncTaskCompletedReq()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_COMPLETED_REQ);
	}

	public SyncTaskCompletedReq(int lastTryTime)
	{
		this();

		this.lastTryTime = lastTryTime;
	}

	@Override
	public SyncTaskCompletedReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.SYNC_TASKPRO_COMPLETED_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tLastTryTime = tlv.getChild(1);
		lastTryTime = TlvByteUtil.byte2Int(tLastTryTime.getValue());
		logger.debug("lastTryTime: " + lastTryTime);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tLastTryTime = new TlvObject(2, 4, TlvByteUtil.int2Byte(lastTryTime));

		TlvObject tlv = new TlvObject(Command.SYNC_TASKPRO_COMPLETED_REQ);
		tlv.push(tSequence);
		tlv.push(tLastTryTime);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_TASKPRO_COMPLETED_REQ
				+ ", child=2, length=" + tlv.getLength() + ")");

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

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskCompletedReq.class);

}
