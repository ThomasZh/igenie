package com.oct.ga.comm.cmd.base;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class QueryActivityBadgeNumberResp
		extends RespCommand
{
	public QueryActivityBadgeNumberResp()
	{
		this.setTag(Command.QUERY_ACTIVITY_BADGE_NUMBER_RESP);
	}

	public QueryActivityBadgeNumberResp(short respState, int timestamp, String jsonArray)
	{
		this();

		this.setRespState(respState);
		this.setTimestamp(timestamp);
		this.setJsonArray(jsonArray);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tRespState = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTimestamp = new TlvObject(3, 4, TlvByteUtil.int2Byte(this.getTimestamp()));
		TlvObject tJson = new TlvObject(4, this.getJsonArray());

		TlvObject tlv = new TlvObject(Command.QUERY_ACTIVITY_BADGE_NUMBER_RESP);
		tlv.push(tSequence);
		tlv.push(tRespState);
		tlv.push(tTimestamp);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.QUERY_ACTIVITY_BADGE_NUMBER_RESP
				+ ", child=4, length=" + tlv.getLength() + ")");

		return tlv;
	}

	private String jsonArray;
	private int timestamp;

	public String getJsonArray()
	{
		return jsonArray;
	}

	public void setJsonArray(String jsonArray)
	{
		this.jsonArray = jsonArray;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryActivityBadgeNumberResp.class);

}
