package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncTomorrowTaskResp
		extends RespCommand
{
	public SyncTomorrowTaskResp()
	{
		this.setTag(Command.SYNC_TOMORROW_TASK_RESP);
	}

	public SyncTomorrowTaskResp(short state, String json, int timestamp)
	{
		this();

		this.setRespState(state);
		this.setJson(json);
		this.setTimestamp(timestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tState = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(3, this.getJson());
		TlvObject tCurrentTimestamp = new TlvObject(4, 4, TlvByteUtil.int2Byte(timestamp));

		TlvObject tlv = new TlvObject(Command.SYNC_TOMORROW_TASK_RESP);

		tlv.push(tSequence);
		tlv.push(tState);
		tlv.push(tJson);
		tlv.push(tCurrentTimestamp);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_TOMORROW_TASK_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncTomorrowTaskResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tRespState = tlv.getChild(1);
		short state = TlvByteUtil.byte2Short(tRespState.getValue());
		logger.debug("state: " + state);
		this.setRespState(state);

		TlvObject tJson = tlv.getChild(2);
		this.setJson(new String(tJson.getValue(), "UTF-8"));

		TlvObject tTimestamp = tlv.getChild(3);
		timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		return this;
	}

	private String json;
	private int timestamp;

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTomorrowTaskResp.class);

}
