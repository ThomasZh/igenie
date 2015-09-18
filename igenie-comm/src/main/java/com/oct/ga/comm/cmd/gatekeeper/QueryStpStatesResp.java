package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryStpStatesResp
		extends RespCommand
{
	public QueryStpStatesResp()
	{
		this.setTag(Command.GK_QUERY_STP_STATES_RESP);
	}

	public QueryStpStatesResp(short respState, String json)
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

		TlvObject tlv = new TlvObject(Command.GK_QUERY_STP_STATES_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.GK_QUERY_STP_STATES_RESP + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryStpStatesResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());

		TlvObject tRespState = tlv.getChild(1);
		Short respState = TlvByteUtil.byte2Short(tRespState.getValue());
		this.setRespState(respState);

		TlvObject tJson = tlv.getChild(2);
		json = new String(tJson.getValue(), "UTF-8");

		return this;
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

	private final static Logger logger = LoggerFactory.getLogger(QueryStpStatesResp.class);

}
