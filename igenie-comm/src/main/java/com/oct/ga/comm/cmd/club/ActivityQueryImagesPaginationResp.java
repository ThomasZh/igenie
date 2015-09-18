package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryImagesPaginationResp
		extends RespCommand
{
	public ActivityQueryImagesPaginationResp()
	{
		this.setTag(Command.ACTIVITY_QUERY_IMAGES_PAGINATION_RESP);
	}

	public ActivityQueryImagesPaginationResp(short respState, String json)
	{
		this();

		this.setRespState(respState);
		this.json = json;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(3, json);

		TlvObject tlv = new TlvObject(Command.ACTIVITY_QUERY_IMAGES_PAGINATION_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.ACTIVITY_QUERY_IMAGES_PAGINATION_RESP
				+ ", child=3, length=" + tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQueryImagesPaginationResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(1);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(2);
		this.json = new String(tJson.getValue(), "UTF-8");

		return this;
	}

	private String json;

	public String getJson()
	{
		return json;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryImagesPaginationResp.class);

}
