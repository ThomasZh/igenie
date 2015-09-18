package com.oct.ga.comm.cmd.base;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncTimestampResp
		extends RespCommand
{
	public SyncTimestampResp()
	{
		super();

		this.setTag(Command.SYNC_TIMESTAMP_RESP);
	}

	public SyncTimestampResp(short commandTag, int timestamp)
	{
		this();

		this.setCommandTag(commandTag);
		this.setTimestamp(timestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tCommandTag = new TlvObject(1, 2, TlvByteUtil.short2Byte(this.getCommandTag()));
		TlvObject tTimestamp = new TlvObject(2, 4, TlvByteUtil.int2Byte(this.getTimestamp()));

		// 6 + 2 + 6 + 4
		int pkgLen = 18;
		logger.info("from command to tlv package:(tag=" + Command.SYNC_TIMESTAMP_RESP + ", child=2, length=" + pkgLen
				+ ")");
		TlvObject tlv = new TlvObject(Command.SYNC_TIMESTAMP_RESP, pkgLen);
		tlv.add(tCommandTag);
		tlv.add(tTimestamp);
		return tlv;
	}

	private short commandTag;
	private int timestamp;

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public short getCommandTag()
	{
		return commandTag;
	}

	public void setCommandTag(short commandTag)
	{
		this.commandTag = commandTag;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTimestampResp.class);

}
