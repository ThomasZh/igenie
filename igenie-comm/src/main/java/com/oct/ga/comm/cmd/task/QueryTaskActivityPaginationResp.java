package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class QueryTaskActivityPaginationResp
		extends RespCommand
{
	public QueryTaskActivityPaginationResp()
	{
		this.setTag(Command.QUERY_TASK_ACTIVITY_PAGINATION_RESP);
	}

	public QueryTaskActivityPaginationResp(short respState)
	{
		this();

		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(3, this.getJson());

		TlvObject tlv = new TlvObject(Command.QUERY_TASK_ACTIVITY_PAGINATION_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.QUERY_TASK_ACTIVITY_PAGINATION_RESP
				+ ", child=3, length=" + tlv.getLength() + ")");

		return tlv;
	}

	private String json;

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryTaskActivityPaginationResp.class);

}
