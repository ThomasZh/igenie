package com.oct.ga.comm.cmd.addrbook;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncContactReq
		extends ReqCommand
{
	public SyncContactReq()
	{
		super();

		this.setTag(Command.SYNC_CONTACT_REQ);
	}

	public SyncContactReq(int timestamp)
	{
		this();

		this.setLastTryTime(timestamp);
	}

	@Override
	public SyncContactReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.SYNC_CONTACT_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tTimestamp = tlv.getChild(0);
		int timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		this.setLastTryTime(timestamp);
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

	private final static Logger logger = LoggerFactory.getLogger(SyncContactReq.class);

}
