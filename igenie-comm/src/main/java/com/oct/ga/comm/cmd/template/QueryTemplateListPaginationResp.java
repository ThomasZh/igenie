package com.oct.ga.comm.cmd.template;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class QueryTemplateListPaginationResp
		extends RespCommand
{
	public QueryTemplateListPaginationResp()
	{
		super();

		this.setTag(Command.QUERY_TEMPLATE_LIST_PAGINATION_RESP);
	}

	public QueryTemplateListPaginationResp(short respState, String jsonArray)
	{
		this();

		this.setRespState(respState);
		this.setJsonArray(jsonArray);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tRespState = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(3, this.getJsonArray());

		TlvObject tlv = new TlvObject(Command.QUERY_TEMPLATE_LIST_PAGINATION_RESP);
		tlv.push(tSequence);
		tlv.push(tRespState);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.QUERY_TEMPLATE_LIST_PAGINATION_RESP
				+ ", child=3, length=" + tlv.getLength() + ")");

		return tlv;
	}

	private String jsonArray;

	public String getJsonArray()
	{
		return jsonArray;
	}

	public void setJsonArray(String jsonArray)
	{
		this.jsonArray = jsonArray;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryTemplateListPaginationResp.class);
}
