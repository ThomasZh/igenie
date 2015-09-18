package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.MessageBadgeNumberJsonBean;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMessageBadgeNumberResp
		extends RespCommand
{
	public QueryMessageBadgeNumberResp()
	{
		this.setTag(Command.QUERY_MESSAGE_BADGE_NUMBER_RESP);
	}

	public QueryMessageBadgeNumberResp(int sequence, short respState, int timestamp,
			MessageBadgeNumberJsonBean messageBadge)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setTimestamp(timestamp);
		this.setMessageBadge(messageBadge);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tRespState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getTimestamp()));
		Gson gson = new Gson();
		String json = gson.toJson(messageBadge);
		logger.debug("json: " + json);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tRespState);
		tlv.push(tTimestamp);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryMessageBadgeNumberResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + sequence);

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tTimestamp = tlv.getChild(i++);
		timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			messageBadge = gson.fromJson(json, new TypeToken<MessageBadgeNumberJsonBean>()
			{
			}.getType());
		}

		return this;
	}

	private MessageBadgeNumberJsonBean messageBadge;
	private int timestamp;

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public MessageBadgeNumberJsonBean getMessageBadge()
	{
		return messageBadge;
	}

	public void setMessageBadge(MessageBadgeNumberJsonBean messageBadge)
	{
		this.messageBadge = messageBadge;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMessageBadgeNumberResp.class);
}
