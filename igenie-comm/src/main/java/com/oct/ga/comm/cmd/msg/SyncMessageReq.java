package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncMessageReq
		extends ReqCommand
{
	public SyncMessageReq()
	{
		super();

		this.setTag(Command.SYNC_MESSAGE_REQ);
	}

	public SyncMessageReq(int lastTryTime)
	{
		this();

		this.setLastTryTime(lastTryTime);
	}

	@Override
	public SyncMessageReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.SYNC_MESSAGE_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tTimestamp = tlv.getChild(0);
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

	private final static Logger logger = LoggerFactory.getLogger(SyncMessageReq.class);

}
