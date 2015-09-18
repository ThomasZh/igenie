package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncFollowingResp
		extends RespCommand
{
	public SyncFollowingResp()
	{
		this.setTag(Command.SYNC_FOLLOWING_RESP);
	}

	public SyncFollowingResp(int sequence, short respState, String json, int currentTimestamp)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.json = json;
		this.setCurrentTimestamp(currentTimestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(i++, json);
		TlvObject tCurrentTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(currentTimestamp));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);
		tlv.push(tCurrentTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncFollowingResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);

		TlvObject tCurrentTimestamp = tlv.getChild(i++);
		currentTimestamp = TlvByteUtil.byte2Int(tCurrentTimestamp.getValue());
		logger.debug("currentTimestamp: " + currentTimestamp);

		return this;
	}

	private String json;
	private int currentTimestamp;

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	public int getCurrentTimestamp()
	{
		return currentTimestamp;
	}

	public void setCurrentTimestamp(int currentTimestamp)
	{
		this.currentTimestamp = currentTimestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncFollowingResp.class);

}
