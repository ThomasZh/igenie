package com.oct.ga.comm.cmd.msgflow;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMsgFlowPaginationResp
		extends RespCommand
{
	public QueryMsgFlowPaginationResp()
	{
		this.setTag(Command.MESSAGE_FLOW_QUERY_PAGINATION_RESP);
	}

	public QueryMsgFlowPaginationResp(List<MsgFlowBasicInfo> array)
	{
		this();

		this.setArray(array);
	}

	public QueryMsgFlowPaginationResp(int sequence, short respState, List<MsgFlowBasicInfo> array)
	{
		this(array);

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(array);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryMsgFlowPaginationResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			array = gson.fromJson(json, new TypeToken<List<MsgFlowBasicInfo>>()
			{
			}.getType());
		}

		return this;
	}

	List<MsgFlowBasicInfo> array;

	public List<MsgFlowBasicInfo> getArray()
	{
		return array;
	}

	public void setArray(List<MsgFlowBasicInfo> array)
	{
		this.array = array;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMsgFlowPaginationResp.class);

}
